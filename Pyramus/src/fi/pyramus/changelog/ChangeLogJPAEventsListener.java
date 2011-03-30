package fi.pyramus.changelog;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Set;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.metamodel.Attribute;

import fi.pyramus.dao.ChangeLogDAO;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.domainmodel.changelog.ChangeLogEntry;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryEntity;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryType;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.events.EventType;
import fi.pyramus.persistence.events.TrackedEntityUtils;
import fi.pyramus.util.ReflectionApiUtils;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") }, mappedName = "jms/JPAEvents")
public class ChangeLogJPAEventsListener implements MessageListener {

  public void onMessage(Message message) {
    try {
      MapMessage mapMessage = (MapMessage) message;
      
      EventType eventType = EventType.valueOf(mapMessage.getString("eventType"));
      
      switch (eventType) {
        case Create:
          handleCreate(mapMessage);
        break;
        case Update:
          handleUpdate(mapMessage);
        break;
        case Delete:
          handleDelete(mapMessage);
        break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void handleCreate(MapMessage mapMessage) throws JMSException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    ChangeLogDAO changeLogDAO = DAOFactory.getInstance().getChangeLogDAO();

    String entityClassName = mapMessage.getString("entity");
    Object id = mapMessage.getObject("id");
    Date time = new Date(mapMessage.getLong("time"));
    User loggedUser = null; // TODO
    
    
    Class<?> entityClass = Class.forName(entityClassName);
    Object entity = systemDAO.findEntityById(entityClass, id);
    
    // First we need to check if ChangeLogEntryEntity is already in the database
    ChangeLogEntryEntity changeLogEntryEntity = changeLogDAO.findChangeLogEntryEntityByName(entityClassName);
    if (changeLogEntryEntity == null) {
      // if not we need to add it 
      changeLogEntryEntity = changeLogDAO.createChangeLogEntryEntity(entityClassName);
    }
    
    // Then we can add the log entry
    ChangeLogEntry entry = changeLogDAO.createChangeLogEntry(changeLogEntryEntity, ChangeLogEntryType.Create, String.valueOf(id), time, loggedUser);

    // After the entry has been added we can add all initial properties
    Set<Attribute<?, ?>> attributes = systemDAO.getEntityAttributes(entityClass);
    for (Attribute<?, ?> attribute : attributes) {
      String fieldName = attribute.getName();
      
      if (TrackedEntityUtils.isTrackedProperty(entityClassName, fieldName)) {
        Object value = null;
        
        switch (attribute.getPersistentAttributeType()) {
          case BASIC:
            value = ReflectionApiUtils.getObjectFieldValue(entity, fieldName, true);
          break;
          case ONE_TO_ONE:
          case MANY_TO_ONE:
            Object joinedEntity = ReflectionApiUtils.getObjectFieldValue(entity, fieldName, true);
            if (joinedEntity != null) {
              value = getEntityId(attribute.getJavaType(), joinedEntity);
            }
          break;
        }
        
        // We need to check if database already contains this entity property
        ChangeLogEntryEntityProperty changeLogEntryEntityProperty = changeLogDAO.findChangeLogEntryEntityPropertyByEntityAndName(changeLogEntryEntity, fieldName);
        if (changeLogEntryEntityProperty == null) {
          // if not we add it there
          changeLogEntryEntityProperty = changeLogDAO.createChangeLogEntryEntityProperty(changeLogEntryEntity, fieldName);
        }
        
        // After entity property has been resolved we can add the property itself
        changeLogDAO.createChangeLogEntryProperty(entry, changeLogEntryEntityProperty, String.valueOf(value));
      }
    }
  }
  
  private void handleDelete(MapMessage mapMessage) throws JMSException  {
    ChangeLogDAO changeLogDAO = DAOFactory.getInstance().getChangeLogDAO();
    
    String entityClassName = mapMessage.getString("entity");
    Object id = mapMessage.getObject("id");
    Date time = new Date(mapMessage.getLong("time"));
    User loggedUser = null; // TODO
    
    // First we need to check if ChangeLogEntryEntity is already in the database
    ChangeLogEntryEntity changeLogEntryEntity = changeLogDAO.findChangeLogEntryEntityByName(entityClassName);
    if (changeLogEntryEntity == null) {
      // if not we need to add it 
      changeLogEntryEntity = changeLogDAO.createChangeLogEntryEntity(entityClassName);
    }
    
    // Then we can add the log entry it self
    changeLogDAO.createChangeLogEntry(changeLogEntryEntity, ChangeLogEntryType.Delete, String.valueOf(id), time, loggedUser);
  }
  
  private void handleUpdate(MapMessage mapMessage) throws JMSException  {
    ChangeLogDAO changeLogDAO = DAOFactory.getInstance().getChangeLogDAO();
    
    String entityClassName = mapMessage.getString("entity");
    Object id = mapMessage.getObject("id");
    Date time = new Date(mapMessage.getLong("time"));
    User loggedUser = null; // TODO
    
    // First we need to check if ChangeLogEntryEntity is already in the database
    ChangeLogEntryEntity changeLogEntryEntity = changeLogDAO.findChangeLogEntryEntityByName(entityClassName);
    if (changeLogEntryEntity == null) {
      // if not we need to add it 
      changeLogEntryEntity = changeLogDAO.createChangeLogEntryEntity(entityClassName);
    }
    
    // Then we can add the log entry
    ChangeLogEntry entry = changeLogDAO.createChangeLogEntry(changeLogEntryEntity, ChangeLogEntryType.Update, String.valueOf(id), time, loggedUser);

    // After the entry has been added we can add all the changed properties
    
    int fieldCount = mapMessage.getInt("fieldCount");
    for (int i = 0; i < fieldCount; i++) {
      String fieldName = mapMessage.getString("field." + i + ".name");
      Object newValue = mapMessage.getObject("field." + i + ".newValue");      

      // We need to check if database already contains this entity property
      ChangeLogEntryEntityProperty changeLogEntryEntityProperty = changeLogDAO.findChangeLogEntryEntityPropertyByEntityAndName(changeLogEntryEntity, fieldName);
      if (changeLogEntryEntityProperty == null) {
        // if not we add it there
        changeLogEntryEntityProperty = changeLogDAO.createChangeLogEntryEntityProperty(changeLogEntryEntity, fieldName);
      }
      
      // After entity property has been resolved we can add the property itself
      changeLogDAO.createChangeLogEntryProperty(entry, changeLogEntryEntityProperty, String.valueOf(newValue));
    }
  } 
  
  private Object getEntityId(Class<?> entityClass, Object entity) {
    SystemDAO systemDAO = new SystemDAO();
    Attribute<?, ?> idAttribute = systemDAO.getEntityIdAttribute(entityClass);
    try {
      return ReflectionApiUtils.getObjectFieldValue(entity, idAttribute.getName(), true);
    } catch (Exception e) {
      return null;
    }
  } 
}
