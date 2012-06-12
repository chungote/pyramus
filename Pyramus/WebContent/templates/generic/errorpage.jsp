<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title>
    <fmt:message key="generic.errorPage.errorPageTitle">
      <fmt:param>${errorMessage}</fmt:param>
    </fmt:message>
  </title>
  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
</head> 
<body>
  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
  <div class="errorPageTitleContainer">
    <div class="errorPageTitleIconContainer">
      <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-error.png"/>
    </div>
    <div class="errorPageTitle">
      <fmt:message key="generic.errorPage.errorPageTitle">
        <fmt:param>${errorMessage}</fmt:param>
      </fmt:message>
    </div>
  </div>
  
   <div class="errorPageDetailsContainer">
     <div class="errorPageSubTitle">
       <fmt:message key="generic.errorPage.statusCode">
         <fmt:param>${statusCode}</fmt:param>
       </fmt:message>
     </div>
       
     <div class="errorPageStackTraceContainer">
       <div class="errorPageSubTitle"><fmt:message key="generic.errorPage.stackTraceTitle"/></div>
       
       <div class="errorPageStackTrace">
         <div class="errorPageStackTraceElement">${exception}
           <c:forEach var="stackTraceElement" items="${exception.stackTrace}">
             <div class="errorPageStackTraceElement">at ${stackTraceElement}</div>
           </c:forEach>
         </div>
       </div>
     </div>
     
     <div class="errorPageReportContainer">
       <div class="errorPageSubTitle"><fmt:message key="generic.errorPage.reportTitle"/></div> 
       <div class="errorPageReportAdditionalInfoContainer">
         <textarea class="errorPageReportAdditionnalInfo"></textarea>
       </div>
       <div class="errorPageReportButtonContainer">
         <input type="submit" value="<fmt:message key="generic.errorPage.sendReportButton"/>"/>
       </div>
     </div>
   </div> 
  
  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>