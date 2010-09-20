package fi.pyramus.json.settings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.pyramus.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.GradingDAO;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.UserRole;
import fi.pyramus.json.JSONRequestController;

public class EditGradingScaleJSONRequestController implements JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    GradingDAO gradingDAO = DAOFactory.getInstance().getGradingDAO();

    Long gradingScaleId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("gradingScaleId"));
    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    
    GradingScale gradingScale = gradingDAO.getGradingScale(gradingScaleId);
    gradingDAO.updateGradingScale(gradingScale, name, description);
    
    Set<Long> existingGrades = new HashSet<Long>();
    
    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("gradesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "gradesTable." + i;
      
      String gradeIdParam = jsonRequestContext.getRequest().getParameter(colPrefix + ".gradeId");
      Long gradeId = StringUtils.isBlank(gradeIdParam) ? null : NumberUtils.createLong(gradeIdParam);
      String gradeName = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      String gradeQualification = jsonRequestContext.getRequest().getParameter(colPrefix + ".qualification");
      String gradeGPAParam = jsonRequestContext.getRequest().getParameter(colPrefix + ".GPA");
      String gradeDescription = jsonRequestContext.getRequest().getParameter(colPrefix + ".description");
      Boolean passingGrade = "1".equals(jsonRequestContext.getRequest().getParameter(colPrefix + ".passingGrade"));
      Double gradeGPA = StringUtils.isBlank(gradeGPAParam) ? null : NumberUtils.createDouble(gradeGPAParam);
      
      if (gradeId != null) {
        Grade grade = gradingDAO.getGrade(gradeId);
        gradingDAO.updateGrade(grade, gradeName, gradeDescription, passingGrade, gradeGPA, gradeQualification);
        existingGrades.add(grade.getId());
      } else {
        Grade grade = gradingDAO.createGrade(gradingScale, gradeName, gradeDescription, passingGrade, gradeGPA, gradeQualification);
        existingGrades.add(grade.getId());
      }
    }
    
    List<Grade> grades = gradingScale.getGrades();
    for (int i = grades.size() - 1; i >= 0; i--) {
      Grade grade = grades.get(i);
      if (!existingGrades.contains(grade.getId()))
        gradingScale.removeGrade(grade);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
