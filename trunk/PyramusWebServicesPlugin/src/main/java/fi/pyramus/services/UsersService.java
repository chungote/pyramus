package fi.pyramus.services;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EnumType;
import javax.xml.ws.BindingType;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.users.UserEntity;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class UsersService extends PyramusService {

  public UserEntity[] listUsers() {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return (UserEntity[]) EntityFactoryVault.buildFromDomainObjects(userDAO.listAll());
  }

  public UserEntity[] listUsersByUserVariable(@WebParam(name = "key") String key, @WebParam(name = "value") String value) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return (UserEntity[]) EntityFactoryVault.buildFromDomainObjects(userDAO.listByUserVariable(key, value));
  }

  public UserEntity createUser(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName,
      @WebParam(name = "externalId") String externalId, @WebParam(name = "authProvider") String authProvider, @WebParam(name = "role") String role) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    Role userRole = EnumType.valueOf(Role.class, role);
    User user = userDAO.create(firstName, lastName, externalId, authProvider, userRole);
    validateEntity(user);
    return EntityFactoryVault.buildFromDomainObject(user);
  }

  public void updateUser(@WebParam(name = "userId") Long userId, @WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName,
      @WebParam(name = "role") String role) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findById(userId);
    Role userRole = EnumType.valueOf(Role.class, role);
    userDAO.update(user, firstName, lastName, userRole);
    validateEntity(user);
  }

  public UserEntity getUserById(@WebParam(name = "userId") Long userId) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return EntityFactoryVault.buildFromDomainObject(userDAO.findById(userId));
  }

  public UserEntity getUserByExternalId(@WebParam(name = "externalId") String externalId, @WebParam(name = "authProvider") String authProvider) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return EntityFactoryVault.buildFromDomainObject(userDAO.findByExternalIdAndAuthProvider(externalId, authProvider));
  }

  public UserEntity getUserByEmail(@WebParam(name = "email") String email) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return EntityFactoryVault.buildFromDomainObject(userDAO.findByEmail(email));
  }

  public void addUserEmail(@WebParam(name = "userId") Long userId, @WebParam(name = "address") String address) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    fi.pyramus.domainmodel.users.User user = userDAO.findById(userId);
    // TODO contact type, default address
    ContactType contactType = contactTypeDAO.findById(new Long(1));
    Email email = emailDAO.create(user.getContactInfo(), contactType, Boolean.TRUE, address);
    validateEntity(email);
  }

  public void removeUserEmail(@WebParam(name = "userId") Long userId, @WebParam(name = "address") String address) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    fi.pyramus.domainmodel.users.User user = userDAO.findById(userId);
    for (Email email : user.getContactInfo().getEmails()) {
      if (email.getAddress().equals(address)) {
        emailDAO.delete(email);
        break;
      }
    }
  }

  public void updateUserEmail(@WebParam(name = "userId") Long userId, @WebParam(name = "fromAddress") String fromAddress,
      @WebParam(name = "toAddress") String toAddress) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    fi.pyramus.domainmodel.users.User user = userDAO.findById(userId);
    for (Email email : user.getContactInfo().getEmails()) {
      if (email.getAddress().equals(fromAddress)) {
        email = emailDAO.update(email, email.getContactType(), email.getDefaultAddress(), toAddress);
        validateEntity(email);
        break;
      }
    }
  }

  public String getUserVariable(@WebParam(name = "userId") Long userId, @WebParam(name = "key") String key) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    return userVariableDAO.findByUserAndKey(userDAO.findById(userId), key);
  }

  public void setUserVariable(@WebParam(name = "userId") Long userId, @WebParam(name = "key") String key, @WebParam(name = "value") String value) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    userVariableDAO.setUserVariable(userDAO.findById(userId), key, value);
  }

}
