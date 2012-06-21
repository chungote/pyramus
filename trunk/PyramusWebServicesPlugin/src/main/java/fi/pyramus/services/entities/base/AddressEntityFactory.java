package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.services.entities.EntityFactory;

public class AddressEntityFactory implements EntityFactory<AddressEntity> {

  public AddressEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Address address = (Address) domainObject; 
    return new AddressEntity(address.getId(), address.getDefaultAddress(), address.getContactType().getId(), 
        address.getCountry(), address.getCity(), address.getPostalCode(), address.getStreetAddress());
  }

}
