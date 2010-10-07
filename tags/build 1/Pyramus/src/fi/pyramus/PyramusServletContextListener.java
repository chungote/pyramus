package fi.pyramus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.domainmodel.system.Setting;
import fi.pyramus.domainmodel.system.SettingKey;
import fi.pyramus.plugin.PluginDescriptor;
import fi.pyramus.plugin.PluginVault;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.internal.InternalAuthorizationStrategy;

/**
 * The application context listener responsible of initialization and finalization of the
 * application.
 */
public class PyramusServletContextListener implements ServletContextListener {

  /**
   * Called when the application shuts down.
   * 
   * @param ctx The servlet context event
   */
  public void contextDestroyed(ServletContextEvent ctx) {
  }

  /**
   * Called when the application starts. Sets up both Hibernate and the page and JSON mappers needed
   * to serve the client requests.
   * 
   * @param servletContextEvent The servlet context event
   */
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      ServletContext ctx = servletContextEvent.getServletContext();
      String webappPath = ctx.getRealPath("/");
      
      // Load the system settings into the system properties
      loadSystemSettings(System.getProperties());
      
      Properties pageControllers = new Properties();
      Properties jsonControllers = new Properties();
      Properties binaryControllers = new Properties();
      
      // Load default page mappings from properties file
      loadPropertiesFile(ctx, pageControllers, "pagemapping.properties");
      // Load default JSON mappings from properties file
      loadPropertiesFile(ctx, jsonControllers, "jsonmapping.properties");
      // Load default binary mappings from properties file
      loadPropertiesFile(ctx, binaryControllers, "binarymapping.properties");
      
      // Load additional request mappings from plugins
      List<PluginDescriptor> plugins = PluginVault.getInstance().getPlugins();
      for (PluginDescriptor plugin : plugins) {
        Map<String, Class<?>> pageRequestControllers = plugin.getPageRequestControllers();
        if (pageRequestControllers != null) {
          for (String key : pageRequestControllers.keySet()) {
            pageControllers.put(key, pageRequestControllers.get(key).getName());
          }
        }
        
        Map<String, Class<?>> jsonRequestControllers = plugin.getJSONRequestControllers();
        if (jsonRequestControllers != null) {
          for (String key : jsonRequestControllers.keySet()) {
            jsonControllers.put(key, jsonRequestControllers.get(key).getName());
          }
        }
        
        Map<String, Class<?>> binaryRequestControllers = plugin.getBinaryRequestControllers();
        if (binaryRequestControllers != null) {
          for (String key : binaryRequestControllers.keySet()) {
            binaryControllers.put(key, binaryRequestControllers.get(key).getName());
          }
        }
      }
      
      // Initialize the page mapper in order to serve page requests 
      RequestControllerMapper.mapControllers(pageControllers, ".page");

      // Initialize the JSON mapper in order to serve JSON requests 
      RequestControllerMapper.mapControllers(jsonControllers, ".json");

      // Initialize the binary mapper in order to serve binary requests 
      RequestControllerMapper.mapControllers(binaryControllers, ".binary");
      
      // Sets the application directory of the application, used primarily for initial data creation

      System.getProperties().setProperty("appdirectory", webappPath);
      
      // Register internal authorization provider 
      AuthenticationProviderVault.registerAuthorizationProviderClass("internal", InternalAuthorizationStrategy.class);
      
      // Initializes all configured authorization strategies
      AuthenticationProviderVault.getInstance().initializeStrategies();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ExceptionInInitializerError(e);
    }
  }

  private void loadPropertiesFile(ServletContext servletContext, Properties properties, String filename)
      throws FileNotFoundException, IOException {
    String webappPath = servletContext.getRealPath("/");
    File settingsFile = new File(webappPath + filename);
    if (settingsFile.canRead()) {
      properties.load(new FileReader(settingsFile));
    }
  }
  
  private void loadSystemSettings(Properties properties) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    
    List<SettingKey> settingKeys = systemDAO.listSettingKeys();
    for (SettingKey settingKey : settingKeys) {
      Setting setting = systemDAO.findSettingByKey(settingKey);
      if (setting != null)
        properties.put(settingKey.getName(), setting.getValue());
    } 
  }
}