package fi.pyramus.services.entities.grading;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.domainmodel.grading.Credit;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class CreditEntityFactory implements EntityFactory<CreditEntity> {

  public CreditEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Credit credit = (Credit) domainObject;
    
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    
    switch (credit.getCreditType()) {
      case CourseAssessment:
        return EntityFactoryVault.buildFromDomainObject(courseAssessmentDAO.findById(credit.getId()));
      case TransferCredit:
        return EntityFactoryVault.buildFromDomainObject(transferCreditDAO.findById(credit.getId()));
    }
    
    return null;
  }

}
