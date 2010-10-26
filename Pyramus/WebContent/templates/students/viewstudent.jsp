<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head> 
    <title><fmt:message key="students.viewStudent.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>

    <!-- Used to render memo values with line breaks; for some reason this is the only approach that works -->
    <% pageContext.setAttribute("newLineChar", "\n"); %>

    <script type="text/javascript">
      function setupBasicTab(abstractStudentId, studentId, studentFullName) {
        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsLabel"/>'
        });
    
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsEditStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/editstudent.page?abstractStudent=' + abstractStudentId  
        }));

        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsManageContactEntriesLabel"/>',
          link: GLOBAL_contextPath + '/students/managestudentcontactentries.page?abstractStudent=' + abstractStudentId  
        }));
        
        var gradesTabRelatedActionsHoverMenu = new IxHoverMenu($('gradesTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.gradesTabRelatedActionsLabel"/>'
        });
        
        gradesTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
          text: '<fmt:message key="students.viewStudent.gradesTabRelatedActionsAddTransferCreditLabel"/>',
          onclick: function (event) {
            alert("TODO");
          }
        }));


        var contactLogTabRelatedActionsHoverMenu = new IxHoverMenu($('contactLogTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.contactLogTabRelatedActionsLabel"/>'
        });
    
        contactLogTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.contactLogTabRelatedActionsManageContactEntriesLabel"/>',
          link: GLOBAL_contextPath + '/students/managestudentcontactentries.page?abstractStudent=' + abstractStudentId  
        }));
        
      }

      function setupCoursesTab(studentId) {
        var relatedContainer = $('tabRelatedActionsContainer.' + studentId);
    
        var coursesTable = new IxTable($('coursesTableContainer.' + studentId), {
          id: 'coursesTable.' + studentId,
          columns : [{
            header : '<fmt:message key="students.viewStudent.coursesTableNameHeader"/>',
            left: 8,
            right: 38, 
            dataType: 'text',
            editable: false
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }, {
            width: 30,
            right: 00,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="students.viewStudent.courseTableViewTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/viewcourse.page?course=' + courseId);
            }
          }]
        });

        return coursesTable;
      }

      function setupTransferCreditsTab(studentId) {
        /* TODO: Oppilaitos */
        
        var transferCreditsTable = new IxTable($('transferCreditsTableContainer.' + studentId), {
          id: 'transferCreditsTable.' + studentId,
          columns : [{
            header : '<fmt:message key="students.viewStudent.transferCreditsTableNameHeader"/>',
            left: 8,
            right: 800, 
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableSubjectHeader"/>',
            right: 592, 
            width: 200,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableGradingDateHeader"/>',
            right: 484, 
            width: 100,
            dataType: 'date',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableCourseLengthHeader"/>',
            right: 376, 
            width: 100,
            dataType: 'number',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableGradeHeader"/>',
            right: 268, 
            width: 100,
            dataType: 'number',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableGradingScaleHeader"/>',
            right: 160, 
            width: 100,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableAssessingUserHeader"/>',
            right: 38,
            width: 126,
            dataType: 'text',
            editable: false
          }, {
            width: 30,
            right: 00,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.viewStudent.studentTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              var studentId = table.getCellValue(event.row, table.getNamedColumnIndex('studentId'));
              alert("TODO: modify transfer credits");
            }
          }]
        });

        return transferCreditsTable;
      }
      
      function setupCourseAssessmentsTab(studentId) {
        var courseAssesmentsTable = new IxTable($('courseAssessmentsTableContainer.' + studentId), {
          id: 'courseAssessmentsTable.' + studentId,
          columns : [{
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableNameHeader"/>',
            left: 8,
            right: 800, 
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableSubjectHeader"/>',
            right: 592, 
            width: 200,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableGradingDateHeader"/>',
            right: 484, 
            width: 100,
            dataType: 'date',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableCourseLengthHeader"/>',
            right: 376, 
            width: 100,
            dataType: 'number',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableGradeHeader"/>',
            right: 268, 
            width: 100,
            dataType: 'number',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableGradingScaleHeader"/>',
            right: 160, 
            width: 100,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableAssessingUserHeader"/>',
            right: 38,
            width: 126,
            dataType: 'text',
            editable: false
          }, {
            width: 30,
            right: 00,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.viewStudent.studentTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              var studentId = table.getCellValue(event.row, table.getNamedColumnIndex('studentId'));
              alert("TODO: modify assesments");
            }
          }]
        });
  
        return courseAssesmentsTable;
      }

      function onLoad(event) {
        var coursesTable;
        var transferCreditsTable;
        var courseAssesmentsTable;
        
        <c:forEach var="student" items="${students}">
          // Setup basics
          setupBasicTab(${abstractStudent.id}, ${student.id}, '${student.fullName}'); 

          // Setup course tabs
          coursesTable = setupCoursesTab(${student.id});

          <c:forEach var="studentCourse" items="${courses[student.id]}">
            coursesTable.addRow(['${studentCourse.course.name}', ${studentCourse.course.id}, '']);
          </c:forEach>

          // Setup grade tabs
          transferCreditsTable = setupTransferCreditsTab(${student.id});

          <c:forEach var="studentTransferCredit" items="${transferCredits[student.id]}">
            transferCreditsTable.addRow([
              '${studentTransferCredit.name}',
              '${studentTransferCredit.subject.name}',
              '${studentTransferCredit.date.time}',
              '${studentTransferCredit.length.units}',
              '${studentTransferCredit.grade.name}',
              '${studentTransferCredit.grade.gradingScale.name}',
              '${studentTransferCredit.assessingUser.fullName}',
              '']);
          </c:forEach>

          courseAssesmentsTable = setupCourseAssessmentsTab(${student.id});

          <c:forEach var="studentCourseAssesment" items="${courseAssesments[student.id]}">
             courseAssesmentsTable.addRow([
              '${studentCourseAssesment.course.name}',
              '${studentCourseAssesment.course.subject.name}',
              '${studentCourseAssesment.date.time}',
              '${studentCourseAssesment.course.courseLength.units}',
              '${studentCourseAssesment.grade.name}',
              '${studentCourseAssesment.grade.gradingScale.name}',
              '${studentCourseAssesment.assessingUser.fullName}',
              '']);
          </c:forEach>
        </c:forEach>
        
             
        var tabControl2 = new IxProtoTabs($('studentTabs'));

        <c:forEach var="student" items="${students}">
          var tabControl = new IxProtoTabs($('tabs.${student.id}'));
        </c:forEach>

        <c:if test="${!empty param.activeTab}">
          tabControl.setActiveTab("${param.activeTab}");  
        </c:if>
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="students.viewStudent.pageTitle" /></h1>
  
    <div id="viewStudentViewContainer">
      <div class="genericFormContainer">
        <div class="tabLabelsContainer containsNestedTabs" id="studentTabs">
          <c:forEach var="student" items="${students}">
            <a class="tabLabel" href="#student.${student.id}">
              <c:choose>
                <c:when test="${student.studyProgramme == null}">
                  <fmt:message key="students.viewStudent.noStudyProgrammeTabLabel"/>
                </c:when>
                <c:otherwise>
                  ${student.studyProgramme.name}
                </c:otherwise>
              </c:choose>
              <c:if test="${student.studyEndDate ne null}">*</c:if>
            </a>
          </c:forEach>
        </div>
    
        <c:forEach var="student" items="${students}">
          <div id="student.${student.id}" class="tabContent tabContentNestedTabs">    
  
            <div id="viewStudentViewContainer"> 
              <div class="genericFormContainer"> 
                <div class="tabLabelsContainer" id="tabs.${student.id}">
                  <a class="tabLabel" href="#basic.${student.id}">
                    <fmt:message key="students.viewStudent.basicTabLabel"/>
                  </a>
                  <a class="tabLabel" href="#courses.${student.id}">
                    <fmt:message key="students.viewStudent.coursesTabLabel"/>
                  </a>
                  <a class="tabLabel" href="#grades.${student.id}">
                    <fmt:message key="students.viewStudent.gradesTabLabel"/>
                  </a>
                  <a class="tabLabel" href="#contactlog.${student.id}">
                    <fmt:message key="students.viewStudent.contactLogTabLabel"/>
                  </a>
                </div>
            
                <div id="basic.${student.id}" class="tabContent">    
                  <div id="basicTabRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>
                  
                    <!--  Student Basic Info Starts -->
                    <div class="genericViewInfoWapper" id="studentViewBasicInfoWrapper">
                    
                      <div class="genericFormSection">
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.viewStudent.firstNameTitle"/>
                          <jsp:param name="helpLocale" value="students.viewStudent.firstNameHelp"/>
                        </jsp:include>                                        
                        <div class="genericViewFormDataText">${student.firstName}</div>
                      </div>
            
                      <div class="genericFormSection">  
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.viewStudent.lastNameTitle"/>
                          <jsp:param name="helpLocale" value="students.viewStudent.lastNameHelp"/>
                        </jsp:include>                                        
                        <div class="genericViewFormDataText">${student.lastName}</div>
                      </div>
                    
	                    <c:choose>
		                    <c:when test="${!empty abstractStudent.birthday}">
		                      <div class="genericFormSection">  
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.birthdayTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.birthdayHelp"/>
		                        </jsp:include>                     
		                        <div class="genericViewFormDataText"><fmt:formatDate pattern="dd.MM.yyyy" value="${abstractStudent.birthday}" /></div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <c:choose>
		                    <c:when test="${!empty abstractStudent.socialSecurityNumber}">
		                      <div class="genericFormSection"> 
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.ssecIdTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.ssecIdHelp"/>
		                        </jsp:include>                                        
		                        <div class="genericViewFormDataText">${abstractStudent.socialSecurityNumber}</div>
		                      </div>
		                    </c:when>
	                    </c:choose>
                    
	                    <div class="genericFormSection">  
		                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                      <jsp:param name="titleLocale" value="students.viewStudent.genderTitle"/>
		                      <jsp:param name="helpLocale" value="students.viewStudent.genderHelp"/>
		                    </jsp:include> 
		                    <div class="genericViewFormDataText">                                       
		                    <c:choose>
		                      <c:when test="${abstractStudent.sex != 'FEMALE'}">
		                        <fmt:message key="students.viewStudent.genderMaleTitle"/>
		                      </c:when>
		                      <c:otherwise>
		                        <fmt:message key="students.viewStudent.genderFemaleTitle"/>
		                      </c:otherwise>
		                    </c:choose>
		                    </div>
		                  </div>
                    
	                    <c:choose>
		                    <c:when test="${!empty student.nickname}">
		                      <div class="genericFormSection">  
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.nicknameTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.nicknameHelp"/>
		                        </jsp:include>                                        
		                        <div class="genericViewFormDataText">${student.nickname}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
                    
                    </div>
                    <!--  Student Basic Info Ends -->
                    
                    <!--  Student Contact Info Starts -->
                    <div class="genericViewInfoWapper" id="studentViewContactInfoWrapper">
                    
                      <c:if test="${!empty student.contactInfo.addresses}">
                        <div class="genericFormSection">  
                          <c:forEach var="address" items="${student.contactInfo.addresses}">
                            <div class="genericFormTitle">
                              <div class="genericFormTitleText">
                                <div>${address.contactType.name}</div>
                              </div>
                            </div>
                            <div class="genericViewFormDataText">
                              <div>${address.name}</div>
                              <div>${address.streetAddress}</div>
                              <div>${address.postalCode} ${address.city}</div>
                              <div>${address.country}</div>
                            </div>
                          </c:forEach>
                        </div>
                      </c:if>
          
                      <div class="genericFormSection">  
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.viewStudent.emailTitle"/>
                          <jsp:param name="helpLocale" value="students.viewStudent.emailHelp"/>
                        </jsp:include>  
                        <div class="genericViewFormDataText">
                        <c:forEach var="email" items="${student.contactInfo.emails}">
                          <c:choose>
                            <c:when test="${not empty email.contactType}">
                              <div><a href="mailto:${email.address}">${email.address}</a> (${fn:toLowerCase(email.contactType.name)})</div>
                            </c:when>
                            <c:otherwise>
                              <div><a href="mailto:${email.address}">${email.address}</a></div>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                        </div>
                      </div>
          
                      <c:choose>
                        <c:when test="${!empty student.contactInfo.phoneNumbers}">
                          <div class="genericFormSection">    
                            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                              <jsp:param name="titleLocale" value="students.viewStudent.phoneNumberTitle"/>
                              <jsp:param name="helpLocale" value="students.viewStudent.phoneNumberHelp"/>
                            </jsp:include>  
                            <div class="genericViewFormDataText">
                            <c:forEach var="phone" items="${student.contactInfo.phoneNumbers}">
                              <c:choose>
                                <c:when test="${not empty phone.contactType}">
                                  <div>${phone.number} (${fn:toLowerCase(phone.contactType.name)})</div>
                                </c:when>
                                <c:otherwise>
                                  <div>${phone.number}</div>
                                </c:otherwise>
                              </c:choose>
                            </c:forEach>
                            </div>
                          </div>
                        </c:when>
                      </c:choose>
                      
                      <c:choose>
		                    <c:when test="${!empty student.municipality}">
		                      <div class="genericFormSection">      
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.municipalityTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.municipalityHelp"/>
		                        </jsp:include>  
		                        <div class="genericViewFormDataText">${student.municipality.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
                    
                    </div>
                    <!--  Student Contact Info Ends -->
                    
                    <div class="columnClear"></div>
                    
                    <!--  Student Education Info Starts -->
                    <div class="genericViewInfoWapper" id="studentViewEducationInfoWrapper">
                    
                      <c:choose>
                        <c:when test="${!empty student.examinationType}">
                          <div class="genericFormSection">         
                            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                              <jsp:param name="titleLocale" value="students.viewStudent.examinationTypeTitle"/>
                              <jsp:param name="helpLocale" value="students.viewStudent.examinationTypeHelp"/>
                            </jsp:include>    
                            <div class="genericViewFormDataText">${student.examinationType.name}</div>
                          </div>
                        </c:when>
                      </c:choose>
                      
                      <c:choose>
		                    <c:when test="${!empty student.educationalLevel}">
		                      <div class="genericFormSection">         
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.educationalLevelTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.educationalLevelHelp"/>
		                        </jsp:include>      
		                        <div class="genericViewFormDataText">${student.educationalLevel.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		
		                  <c:choose>
		                    <c:when test=">${!empty student.education}">
		                      <div class="genericFormSection">             
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.educationTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.educationHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.education}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		        
		                  <c:choose>
		                    <c:when test="${!empty student.studyProgramme}">
		                      <div class="genericFormSection">             
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studyProgrammeTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studyProgrammeHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.studyProgramme.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		
		                  <c:choose>
		                    <c:when test="${fn:length(studentGroups[student.id]) gt 0}">
		                      <div class="genericFormSection">             
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studentGroupTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studentGroupHelp"/>
		                        </jsp:include>
		                        
		                        <div class="genericViewFormDataText">
		                          <c:forEach var="studentGroup" items="${studentGroups[student.id]}" varStatus="sgStat">
		                            ${studentGroup.name}<c:if test="${!sgStat.last}">, </c:if>
		                          </c:forEach>
		                        </div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		
		                  <c:choose>
		                    <c:when test="${!empty student.school}">
		                      <div class="genericFormSection">             
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.schoolTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.schoolHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.school.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <c:choose>
		                    <c:when test="${!empty student.previousStudies}">
		                      <div class="genericFormSection">                   
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.previousStudiesTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.previousStudiesHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.previousStudies}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		        
		                  <c:choose>
		                    <c:when test="${!empty student.studyTimeEnd}">
		                      <div class="genericFormSection">                     
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studyTimeEndTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studyTimeEndHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.studyTimeEnd}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		        
		                  <c:choose>
		                    <c:when test="${!empty student.studyStartDate}">
		                      <div class="genericFormSection">                       
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studyStartDateTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studyStartDateHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.studyStartDate}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <c:choose>
		                    <c:when test="${!empty student.studyEndDate}">
		                      <div class="genericFormSection">                        
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studyEndDateTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studyEndDateHelp"/>
		                        </jsp:include> 
		                        <div class="genericViewFormDataText">${student.studyEndDate}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		        
		                  <c:choose>
		                    <c:when test="${!empty student.studyEndReason}">
		                      <div class="genericFormSection">                          
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studyEndReasonTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studyEndReasonHelp"/>
		                        </jsp:include> 
		                        <div class="genericViewFormDataText">${student.studyEndReason.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		        
		                  <c:choose>
		                    <c:when test="${!empty student.studyEndText}">
		                      <div class="genericFormSection">                            
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.studyEndTextTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.studyEndTextHelp"/>
		                        </jsp:include> 
		                        <div class="genericViewFormDataText">${student.studyEndText}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
                    
                    </div>
                    <!--  Student Education Info Ends -->
                    
                    <!--  Student Additional Info Starts -->
                    <div class="genericViewInfoWapper" id="studentViewAdditionalInfoWrapper">
                    
                      <c:choose>
		                    <c:when test="${not empty student.tags}">
		                      <div class="genericFormSection">
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.tagsTitle" />
		                          <jsp:param name="helpLocale" value="students.viewStudent.tagsHelp" />
		                        </jsp:include>
		                        <div class="genericViewFormDataText">
		                        <c:forEach var="tag" items="${student.tags}" varStatus="vs">
		                          <c:out value="${tag.text}"/>
		                          <c:if test="${not vs.last}"><c:out value=" "/></c:if>
		                        </c:forEach>
		                        </div>
		                      </div>
		                    </c:when>
		                  </c:choose> 
		                  
		                  <c:choose>
		                    <c:when test="${!empty student.language}">
		                      <div class="genericFormSection">      
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.languageTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.languageHelp"/>
		                        </jsp:include>    
		                        <div class="genericViewFormDataText">${student.language.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <c:choose>
		                    <c:when test="${!empty student.nationality}">
		                      <div class="genericFormSection">        
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.nationalityTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.nationalityHelp"/>
		                        </jsp:include>    
		                        <div class="genericViewFormDataText">${student.nationality.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		        
		                  <c:choose>
		                    <c:when test="${!empty student.activityType}">
		                      <div class="genericFormSection">          
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.activityTypeTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.activityTypeHelp"/>
		                        </jsp:include>    
		                        <div class="genericViewFormDataText">${student.activityType.name}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <c:choose>
		                    <c:when test="${!empty student.additionalInfo}">
		                      <div class="genericFormSection">               
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.additionalInformationTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.additionalInformationHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${student.additionalInfo}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <c:choose>
		                    <c:when test="${!empty student.contactInfo.additionalInfo}">
		                      <div class="genericFormSection">                 
		                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                          <jsp:param name="titleLocale" value="students.viewStudent.additionalContactInfoTitle"/>
		                          <jsp:param name="helpLocale" value="students.viewStudent.additionalContactInfoHelp"/>
		                        </jsp:include>
		                        <div class="genericViewFormDataText">${fn:replace(student.contactInfo.additionalInfo, newLineChar, "<br/>")}</div>
		                      </div>
		                    </c:when>
		                  </c:choose>
		                  
		                  <div class="genericFormSection">                            
		                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
		                      <jsp:param name="titleLocale" value="students.viewStudent.lodgingTitle"/>
		                      <jsp:param name="helpLocale" value="students.viewStudent.lodgingHelp"/>
		                    </jsp:include> 
		                    <div class="genericViewFormDataText">
		                      <c:choose>
		                        <c:when test="${student.lodging}">
		                          <fmt:message key="students.viewStudent.lodgingYes"/>
		                        </c:when>
		                        <c:otherwise>
		                          <fmt:message key="students.viewStudent.lodgingNo"/>
		                        </c:otherwise> 
		                      </c:choose>
		                    </div>
		                  </div>
		                
                    </div>
                    <!--  Student Additional Info Ends -->
                    
                    <div class="columnClear"></div>
                  
                </div>
        
                <div id="courses.${student.id}" class="tabContent">
                  <div id="coursesTabRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>
                  
                  <div class="genericFormSection">                              
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="students.viewStudent.coursesTitle"/>
                      <jsp:param name="helpLocale" value="students.viewStudent.coursesHelp"/>
                    </jsp:include> 
                    <div><div id="coursesTableContainer.${student.id}"></div></div>
                  </div>
                </div>
  
                <div id="grades.${student.id}" class="tabContent">
                  <div id="gradesTabRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>
                  
                  <div class="genericFormSection">                                
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="students.viewStudent.courseAssessmentsTitle"/>
                      <jsp:param name="helpLocale" value="students.viewStudent.courseAssessmentsHelp"/>
                    </jsp:include> 
                    <div><div id="courseAssessmentsTableContainer.${student.id}"></div></div>
                  </div>
                  
                  <div class="genericFormSection">                                  
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="students.viewStudent.transferCreditsTitle"/>
                      <jsp:param name="helpLocale" value="students.viewStudent.transferCreditsHelp"/>
                    </jsp:include> 
                    <div><div id="transferCreditsTableContainer.${student.id}"></div></div>
                  </div>
                </div> 

                <div id="contactlog.${student.id}" class="tabContent">
                  <div id="contactLogTabRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>

                  <div id="studentContactEntryList.${student.id}" class="studentContactEntryWrapper">
                    <c:forEach var="contactEntry" items="${contactEntries[student.id]}">
                      <div id="studentContactEntryItem" class="studentContactEntryItem">
                        <div>
                          <span class="studentContactEntryDate"><fmt:formatDate pattern="dd.MM.yyyy" value="${contactEntry.entryDate}" /></span>
                          <span class="studentContactEntryType">
                          <c:choose>
                            <c:when test="${contactEntry.type eq 'OTHER'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.other"/>
                            </c:when>
                            <c:when test="${contactEntry.type eq 'LETTER'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.letter"/>
                            </c:when>
                            <c:when test="${contactEntry.type eq 'EMAIL'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.email"/>
                            </c:when>
                            <c:when test="${contactEntry.type eq 'PHONE'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.phone"/>
                            </c:when>
                            <c:when test="${contactEntry.type eq 'CHATLOG'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.chatlog"/>
                            </c:when>
                            <c:when test="${contactEntry.type eq 'SKYPE'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.skype"/>
                            </c:when>
                            <c:when test="${contactEntry.type eq 'FACE2FACE'}">
                              <fmt:message key="students.viewStudent.contactEntry.types.face2face"/>
                            </c:when>
                          </c:choose>
                          </span>
                          <span class="studentContactEntryCreator">${contactEntry.creatorName}</span> 
                        </div>              
                        <div>
                          ${contactEntry.text}
                        </div>
                      </div>
                    </c:forEach>
                  </div>
                </div>
              </div>
            </div>  
          </div>
        </c:forEach>
      </div>
    </div>  

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>