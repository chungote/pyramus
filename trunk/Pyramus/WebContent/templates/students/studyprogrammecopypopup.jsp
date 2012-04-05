<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
  </head>
  <body>
    <div class="studyProgrammeCopyPopupContainer">
      <div><fmt:message key="students.copyStudyProgrammePopup.dialogMessage">
        <fmt:param>${courseAssessmentCount + transferCreditCount + creditLinkCount}</fmt:param>
      </fmt:message></div>
      <div><input type="checkbox" id="linkStudentCreditsCheckbox" name="linkStudentCreditsCheckbox" value="1"/> <fmt:message key="students.copyStudyProgrammePopup.checkboxCaption"/></div>
    </div>
  </body>
</html>