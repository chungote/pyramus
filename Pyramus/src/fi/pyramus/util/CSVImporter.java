package fi.pyramus.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;
import fi.pyramus.ErrorLevel;
import fi.pyramus.PyramusRuntimeException;
import fi.pyramus.StatusCode;
import fi.pyramus.I18N.Messages;
import fi.pyramus.util.dataimport.DataImportContext;
import fi.pyramus.util.dataimport.DataImportStrategyProvider;
import fi.pyramus.util.dataimport.EntityHandlingStrategy;

public class CSVImporter {

  private String[] headerFields;

  public List<Object> importDataFromStream(InputStream stream, String entityStrategy, Long loggedUserId, Locale locale) throws UnsupportedEncodingException {
    List<Object> list = new ArrayList<Object>();
    CSVReader reader = new CSVReader(new InputStreamReader(stream, "UTF-8"));
    
    try {
      String [] firstLine = reader.readNext();
      this.headerFields = firstLine;
      String [] nextLine;
      int rowNum = 1;
      EntityHandlingStrategy entityHandler = DataImportStrategyProvider.instance().getEntityHandler(entityStrategy);

      if (firstLine != null) {
        while ((nextLine = reader.readNext()) != null) {
          
          if (firstLine.length != nextLine.length) {
            throw new PyramusRuntimeException(ErrorLevel.INFORMATION, StatusCode.VALIDATION_FAILURE, 
                Messages.getInstance().getText(locale, "system.importcsv.invalidNumberOfArguments", new Object[] { rowNum }));
          }
          
          DataImportContext context = new DataImportContext(loggedUserId);
          context.setFields(firstLine);
          context.setFieldValues(nextLine);
          entityHandler.initializeContext(context);
          
  
          String value = null;
          String fieldName = null;
          
          for (int i = 0; i < firstLine.length; i++) {
            fieldName = firstLine[i];
            value = nextLine[i];

            if ((!StringUtils.isBlank(fieldName)) && (!StringUtils.isBlank(value)))
              entityHandler.handleValue(fieldName, value, context);
          }
          
          entityHandler.saveEntities(context);
          
          Object[] entities2 = context.getEntities(); 
          for (int i = 0; i < entities2.length; i++) {
            if (entities2[i].getClass().equals(entityHandler.getMainEntityClass())) {
              list.add(entities2[i]);
            }
          }

          rowNum++;
        }
      }
    } catch (Exception e) {
      throw new PyramusRuntimeException(e);
    }

    return list;
  }

  public String[] getHeaderFields() {
    return headerFields;
  }

  @SuppressWarnings("rawtypes")
  public Class getEntityClassForStrategy(String strategyName) {
    EntityHandlingStrategy entityHandler = DataImportStrategyProvider.instance().getEntityHandler(strategyName);
    return entityHandler.getMainEntityClass();
  }  
}