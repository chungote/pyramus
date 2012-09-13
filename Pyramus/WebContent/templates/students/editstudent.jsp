<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="students.editStudent.pageTitle">
        <fmt:param value="${abstractStudent.latestStudent.fullName}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function addAddressTableRow(addressTable) {
        addressTable.addRow([-1, '', '', '', '', '', '', '', '', '']);
      }

      function addPhoneTableRow(phoneTable) {
        phoneTable.addRow([-1, '', '', '', '', '']);
      }

      function addEmailTableRow(emailTable) {
        emailTable.addRow([-1, '', '', '', '', '']);
      }

      function initPhoneTable(studentId) {
        var phoneTable = new IxTable($('phoneTable.' + studentId), {
          id : "phoneTable." + studentId,
          columns : [ {
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'phoneId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultNumber',
            tooltip: '<fmt:message key="students.editStudent.phoneTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="students.editStudent.phoneTableTypeHeader"/>',
            width: 150,
            left : 30,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="students.editStudent.phoneTableNumberHeader"/>',
            left : 188,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'phone'
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="students.editStudent.phoneTableAddTooltip"/>',
            onclick: function (event) {
              addPhoneTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.phoneTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        phoneTable.addListener("rowAdd", function (event) {
          var phoneTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          phoneTable.showCell(event.row, phoneTable.getNamedColumnIndex(enabledButton));
        });

        return phoneTable;
      }

      function initEmailTable(studentId) {
        var emailTable = new IxTable($('emailTable.' + studentId), {
          id : "emailTable." + studentId,
          columns : [ {
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'emailId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="students.editStudent.emailTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="students.editStudent.emailTableTypeHeader"/>',
            width: 150,
            left : 30,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="students.editStudent.emailTableAddressHeader"/>',
            left : 188,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'email',
            required: true,
            editorClassNames: 'email'
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="students.editStudent.emailTableAddTooltip"/>',
            onclick: function (event) {
              addEmailTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.emailTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        emailTable.addListener("rowAdd", function (event) {
          var emailTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          emailTable.showCell(event.row, emailTable.getNamedColumnIndex(enabledButton));
        });

        return emailTable;
      }

      function initAddressTable(studentId) {
        var addressTable = new IxTable($('addressTable.' + studentId), {
          id : "addressTable." + studentId,
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'addressId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="students.editStudent.addressTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="students.editStudent.addressTableTypeHeader"/>',
            left : 30,
            width : 150,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="students.editStudent.addressTableNameHeader"/>',
            left : 188,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'name'
          }, {
            header : '<fmt:message key="students.editStudent.addressTableStreetHeader"/>',
            left : 344,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'street'
          }, {
            header : '<fmt:message key="students.editStudent.addressTablePostalCodeHeader"/>',
            left : 502,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'postal'
          }, {
            header : '<fmt:message key="students.editStudent.addressTableCityHeader"/>',
            left : 610,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'city'
          }, {
            header : '<fmt:message key="students.editStudent.addressTableCountryHeader"/>',
            left : 768,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'country'
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="students.editStudent.addressTableAddTooltip"/>',
            onclick: function (event) {
              addAddressTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.addressTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        addressTable.addListener("rowAdd", function (event) {
          var addressTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          addressTable.showCell(event.row, addressTable.getNamedColumnIndex(enabledButton));
        });

        return addressTable;
      }

      function initStudentVariableTable(studentId) {
        var variablesTable = new IxTable($('variablesTableContainer.' + studentId), {
          id : "variablesTable." + studentId,
          columns : [{
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.editStudent.variablesTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var valueColumn = table.getNamedColumnIndex('value');
              table.setCellEditable(event.row, valueColumn, table.isCellEditable(event.row, valueColumn) == false);
            }
          }, {
            dataType : 'hidden',
            editable: false,
            paramName: 'key'
          },{
            left : 38,
            width: 150,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 188,
            width : 750,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }]
        });

        return variablesTable;
      }
          
      function setupTags() {
        JSONRequest.request("tags/getalltags.json", {
          onSuccess: function (jsonResponse) {
            <c:forEach var="student" items="${students}">
	            new Autocompleter.Local("tags.${student.id}", "tags_choices.${student.id}", jsonResponse.tags, {
	              tokens: [',', '\n', ' ']
	            });
	          </c:forEach>  
          }
        });   
      }
      
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));

        setupRelatedCommandsBasic();
        setupTags();

        var addressTable;
        var phoneTable;
        var emailTable;
        var variablesTable;
        var value;
        
        <c:forEach var="student" items="${students}">
          <c:choose>
            <c:when test="${student.studyProgramme eq null}">
              <c:set var="sprogName">
                <fmt:message key="students.editStudent.noStudyProgrammeTabLabel" />
              </c:set>
            </c:when>
            <c:otherwise>
              <c:set var="sprogName">${fn:escapeXml(student.studyProgramme.name)}</c:set>
            </c:otherwise>
          </c:choose>
        
          setupRelatedCommands(${student.id}, '${sprogName}', ${studentHasCredits[student.id]});

          // Addresses

          addressTable = initAddressTable(${student.id});
  
          <c:forEach var="address" items="${student.contactInfo.addresses}">
            addressTable.addRow([
              ${address.id},
              ${address.defaultAddress},
              ${address.contactType.id},
              '${fn:escapeXml(address.name)}',
              '${fn:escapeXml(address.streetAddress)}',
              '${fn:escapeXml(address.postalCode)}',
              '${fn:escapeXml(address.city)}',
              '${fn:escapeXml(address.country)}',
              '',
              '']);
          </c:forEach>
    
          if (addressTable.getRowCount() == 0) {
            addAddressTableRow(addressTable);
            addressTable.setCellValue(0, 1, true);
          }

          // E-mail addresses

          emailTable = initEmailTable(${student.id});

          <c:forEach var="email" items="${student.contactInfo.emails}">
            emailTable.addRow([
              ${email.id},
              ${email.defaultAddress},
              ${email.contactType.id},
              '${fn:escapeXml(email.address)}',
              '',
              '']);
          </c:forEach>

          if (emailTable.getRowCount() == 0) {
            addEmailTableRow(emailTable);
            emailTable.setCellValue(0, 1, true);
          }

          // Phones

          phoneTable = initPhoneTable(${student.id});
  
          <c:forEach var="phone" items="${student.contactInfo.phoneNumbers}">
            phoneTable.addRow([
              ${phone.id},
              ${phone.defaultNumber},
              ${phone.contactType.id},
              '${fn:escapeXml(phone.number)}',
              '',
              '']);
          </c:forEach>
    
          if (phoneTable.getRowCount() == 0) {
            addPhoneTableRow(phoneTable);
            phoneTable.setCellValue(0, 1, true);
          }

          <c:choose>
            <c:when test="${fn:length(variableKeys) gt 0}">
              // Student variables
              variablesTable = initStudentVariableTable(${student.id});
    
              <c:forEach var="variableKey" items="${variableKeys}">
                value = '${fn:escapeXml(student.variablesAsStringMap[variableKey.variableKey])}';
                var rowNumber = variablesTable.addRow([
                  '',
                  '${fn:escapeXml(variableKey.variableKey)}',
                  '${fn:escapeXml(variableKey.variableName)}',
                  value
                ]);
        
                var dataType;
                <c:choose>
                  <c:when test="${variableKey.variableType == 'NUMBER'}">
                    dataType = 'number';
                  </c:when>
                  <c:when test="${variableKey.variableType == 'DATE'}">
                    dataType = 'date';
                  </c:when>
                  <c:when test="${variableKey.variableType == 'BOOLEAN'}">
                    dataType = 'checkbox';
                  </c:when>
                  <c:otherwise>
                    dataType = 'text';
                  </c:otherwise>
                </c:choose>
                
                variablesTable.setCellDataType(rowNumber, 3, dataType);
              </c:forEach>
            </c:when>
          </c:choose>
        </c:forEach>
      }

      function setupRelatedCommandsBasic() {
        var relatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="students.editStudent.basicTabRelatedActionsLabel"/>'
        });
    
        relatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="students.editStudent.basicTabRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?abstractStudent=${abstractStudent.id}');
          }
        }));

        relatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.editStudent.basicTabRelatedActionsManageContactEntriesLabel"/>',
          link: GLOBAL_contextPath + '/students/managestudentcontactentries.page?abstractStudent=${abstractStudent.id}'  
        }));
      }

      function setupRelatedCommands(studentId, studyProgrammeName, studentHasCredits) {
        var studentRelatedActionsHoverMenu = new IxHoverMenu($('studentRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsLabel"/>'
        });
    
        studentRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsCopyAsNewStudyProgrammeLabel"/>',
          onclick: function (event) {

            if (studentHasCredits) {
              var dialog = new IxDialog({
                id : 'chooseCopyMethod',
                contentURL : GLOBAL_contextPath + '/students/studyprogrammecopydialog.page?student=' + studentId,
                centered : true,
                showOk : true,  
                showCancel : true,
                title : '<fmt:message key="students.copyStudyProgrammePopup.dialogTitle"/>',
                okLabel : '<fmt:message key="students.copyStudyProgrammePopup.okLabel"/>',
                cancelLabel : '<fmt:message key="students.copyStudyProgrammePopup.cancelLabel"/>'
              });
            
              dialog.setSize("300px", "160px");
              dialog.addDialogListener( function(event) {
                var dlg = event.dialog;
            
                switch (event.name) {
                  case 'okClick':
                    var pelem = $(dlg.getContentDocument().documentElement);
                    var cbox = pelem.down("input[name='linkStudentCreditsCheckbox']");
                    var linkCredits = cbox.checked == true ? true : false;
                    
                    JSONRequest.request("students/copystudyprogramme.json", {
                      parameters: {
                        studentId: studentId,
                        linkCredits: linkCredits
                      },
                      onSuccess: function (jsonResponse) {
                        window.location.reload();
                      }
                    });   
                  break;
                }
              });
            
              dialog.open();
            } else {
              JSONRequest.request("students/copystudyprogramme.json", {
                parameters: {
                  studentId: studentId,
                  linkCredits: false
                },
                onSuccess: function (jsonResponse) {
                  window.location.reload();
                }
              }); 
            }
          }
        }));       
    
        studentRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsManageTransferCreditsLabel"/>',
          link: GLOBAL_contextPath + '/grading/managetransfercredits.page?studentId=' + studentId  
        }));      

        studentRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/edit-delete.png',
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsArchiveStudentLabel"/>',
          onclick: function (event) {
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=students.editStudent.archiveStudentConfirmDialogContent&localeParams=" + encodeURIComponent(studyProgrammeName);
            
            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,  
              showCancel : true,
              autoEvaluateSize: true,
              title : '<fmt:message key="students.editStudent.archiveStudentConfirmDialogTitle"/>',
              okLabel : '<fmt:message key="students.editStudent.archiveStudentConfirmDialogOkLabel"/>',
              cancelLabel : '<fmt:message key="students.editStudent.archiveStudentConfirmDialogCancelLabel"/>'
            });
          
            dialog.addDialogListener( function(event) {
              var dlg = event.dialog;
          
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("students/archivestudent.json", {
                    parameters: {
                      student: studentId
                    },
                    onSuccess: function (jsonResponse) {
                      window.location.reload();
                    }
                  });   
                break;
              }
            });
          
            dialog.open(); 
          }
        }));
      }
    </script>
  </head>

  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">
      <fmt:message key="students.editStudent.pageTitle">
        <fmt:param value="${abstractStudent.latestStudent.fullName}"/>
      </fmt:message>
    </h1>

    <div id="editStudentEditFormContainer"> 
      <div class="genericFormContainer"> 

        <form action="editstudent.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="version" value="${abstractStudent.version}"/>
        
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="students.editStudent.studentBasicInfoTabLabel"/>
            </a>

            <c:forEach var="student" items="${students}">
              <a class="tabLabel" href="#student.${student.id}">
                <c:choose>
                  <c:when test="${student.studyProgramme == null}">
                     <fmt:message key="students.editStudent.noStudyProgrammeTabLabel"/>
                  </c:when>
                  <c:otherwise>
                    ${student.studyProgramme.name}
                  </c:otherwise>
                </c:choose>
                
                <c:if test="${student.hasFinishedStudies}">*</c:if>
              </a>
            </c:forEach>
            <ix:extensionHook name="students.editStudent.tabLabels"/>
          </div>
          
          <div id="basic" class="tabContent">    
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            
            <input type="hidden" name="abstractStudentId" value="${abstractStudent.id}"/>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.firstNameTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.firstNameHelp"/>
              </jsp:include>            
              ${abstractStudent.latestStudent.firstName}
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.lastNameTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.lastNameHelp"/>
              </jsp:include>            
              ${abstractStudent.latestStudent.lastName}
            </div>
            
            <div class="genericFormSection">   
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.birthdayTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.birthdayHelp"/>
              </jsp:include>            
              <input type="text" name="birthday" class="ixDateField" value="${abstractStudent.birthday.time}">
            </div>
      
            <div class="genericFormSection">     
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.ssecIdTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.ssecIdHelp"/>
              </jsp:include>            
              <input type="text" name="ssecId" value="${abstractStudent.socialSecurityNumber}" size="15" class="mask" ix:validatemask="^([0-9]{6})[-A]([0-9A-Z]{4})$">
            </div>

            <div class="genericFormSection">       
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.genderTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.genderHelp"/>
              </jsp:include>            
              <select name="gender">
                <option value="male" <c:if test="${abstractStudent.sex == 'MALE'}">selected="selected"</c:if>><fmt:message key="students.editStudent.genderMaleTitle"/></option>
                <option value="female" <c:if test="${abstractStudent.sex == 'FEMALE'}">selected="selected"</c:if>><fmt:message key="students.editStudent.genderFemaleTitle"/></option>
              </select>
            </div>

            <div class="genericFormSection">       
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.secureInfoTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.secureInfoHelp"/>
              </jsp:include>
              <c:choose>
                <c:when test="${abstractStudent.secureInfo}"><input type="checkbox" name="secureInfo" value="true" checked="checked"/></c:when>
                <c:otherwise><input type="checkbox" name="secureInfo" value="true"/></c:otherwise>              
              </c:choose>
              <fmt:message key="students.editStudent.secureInfoCheckboxLabel"/>
            </div>

            <div class="genericFormSection">         
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.abstractStudentBasicInfoTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.abstractStudentBasicInfoHelp"/>
              </jsp:include>            
              <textarea name="basicInfo" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true">${abstractStudent.basicInfo}</textarea>
            </div>
            <ix:extensionHook name="students.editStudent.tabs.basic"/>
          </div>

          <c:forEach var="student" items="${students}">
            <div id="student.${student.id}" class="tabContent">
              <input type="hidden" name="studentVersion.${student.id}" value="${student.version}"/>
                  
              <div id="studentRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>

              <div class="genericFormSection">           
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyProgrammeTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyProgrammeHelp"/>
                </jsp:include>            
                <select class="required" name="studyProgramme.${student.id}">
                  <option></option>           
                  <c:forEach var="studyProgramme" items="${studyProgrammes}">
                    <c:choose>
                      <c:when test="${studyProgramme.id eq student.studyProgramme.id}">
                        <option value="${studyProgramme.id}" selected="selected">${studyProgramme.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${studyProgramme.id}">${studyProgramme.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                  <c:if test="${student.studyProgramme.archived}">
                    <option value="${student.studyProgramme.id}" selected="selected">${student.studyProgramme.name}*</option>
                  </c:if>
                </select>
              </div>
  
              <div class="genericFormSection">           
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.firstNameTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.firstNameHelp"/>
                </jsp:include>            
                <input type="text" class="required" name="firstName.${student.id}" value="${fn:escapeXml(student.firstName)}" size="20">
              </div>
    
              <div class="genericFormSection">             
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.lastNameTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.lastNameHelp"/>
                </jsp:include>            
                <input type="text" class="required" name="lastName.${student.id}" value="${fn:escapeXml(student.lastName)}" size="30">
              </div>
              
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.nicknameTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.nicknameHelp"/>
                </jsp:include>          
                <input type="text" name="nickname.${student.id}" value="${fn:escapeXml(student.nickname)}" size="30">                                 
              </div>

	            <div class="genericFormSection">
	              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
	                <jsp:param name="titleLocale" value="students.editStudent.tagsTitle"/>
	                <jsp:param name="helpLocale" value="students.editStudent.tagsHelp"/>
	              </jsp:include>
	              <input type="text" id="tags.${student.id}" name="tags.${student.id}" size="40" value="${fn:escapeXml(tags[student.id])}"/>
	              <div id="tags_choices.${student.id}" class="autocomplete_choices"></div>
	            </div>
            
              <div class="genericFormSection">                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.addressesTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.addressesHelp"/>
                </jsp:include>
                <div id="addressTable.${student.id}"></div>
              </div>

              <div class="genericFormSection">               
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.emailTableEmailsTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.emailTableEmailsHelp"/>
                </jsp:include>
                <div id="emailTable.${student.id}"></div>
              </div>

              <div class="genericFormSection">                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.phoneNumbersTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.phoneNumbersHelp"/>
                </jsp:include>
                <div id="phoneTable.${student.id}"></div>
              </div>
      
              <div class="genericFormSection">                                  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.otherContactInfoTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.otherContactInfoInfoHelp"/>
                </jsp:include>
                <textarea name="otherContactInfo.${student.id}" rows="5" cols="50">${student.contactInfo.additionalInfo}</textarea>
              </div>

              <div class="genericFormSection">                    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.municipalityTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.municipalityHelp"/>
                </jsp:include>
                  <select name="municipality.${student.id}">           
                  <option></option>  
                  <c:forEach var="municipality" items="${municipalities}">
                    <c:choose>
                      <c:when test="${municipality.id eq student.municipality.id}">
                        <option value="${municipality.id}" selected="selected">${municipality.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${municipality.id}">${municipality.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
    
              <div class="genericFormSection">                      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.languageTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.languageHelp"/>
                </jsp:include>
                <select name="language.${student.id}">           
                  <option></option>  
                  <c:forEach var="language" items="${languages}">
                    <c:choose>
                      <c:when test="${language.id eq student.language.id}">
                        <option value="${language.id}" selected="selected">${language.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${language.id}">${language.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
    
              <div class="genericFormSection">                        
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.nationalityTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.nationalityHelp"/>
                </jsp:include>
                <select name="nationality.${student.id}">
                  <option></option>  
                  <c:forEach var="nationality" items="${nationalities}">
                    <c:choose>
                      <c:when test="${nationality.id eq student.nationality.id}">
                        <option value="${nationality.id}" selected="selected">${nationality.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${nationality.id}">${nationality.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
    
              <div class="genericFormSection">                          
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.activityTypeTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.activityTypeHelp"/>
                </jsp:include>
                <select name="activityType.${student.id}">
                  <option></option>  
                  <c:forEach var="activityType" items="${activityTypes}">
                    <c:choose>
                      <c:when test="${activityType.id eq student.activityType.id}">
                        <option value="${activityType.id}" selected="selected">${activityType.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${activityType.id}">${activityType.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">                            
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.examinationTypeTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.examinationTypeHelp"/>
                </jsp:include>
                <select name="examinationType.${student.id}">
                  <option></option>  
                  <c:forEach var="examinationType" items="${examinationTypes}">
                    <c:choose>
                      <c:when test="${examinationType.id eq student.examinationType.id}">
                        <option value="${examinationType.id}" selected="selected">${examinationType.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${examinationType.id}">${examinationType.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">                              
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.educationalLevelTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.educationalLevelHelp"/>
                </jsp:include>
                <select name="educationalLevel.${student.id}">
                  <option></option>  
                  <c:forEach var="educationalLevel" items="${educationalLevels}">
                    <c:choose>
                      <c:when test="${educationalLevel.id eq student.educationalLevel.id}">
                        <option value="${educationalLevel.id}" selected="selected">${educationalLevel.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${educationalLevel.id}">${educationalLevel.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">                                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.schoolTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.schoolHelp"/>
                </jsp:include>
            
                <select name="school.${student.id}">
                  <option value="-1"></option>           
                  <c:forEach var="school" items="${schools}">
                    <c:choose>
                      <c:when test="${school.id eq student.school.id}">
                        <option value="${school.id}" selected="selected">${school.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${school.id}">${school.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                  <c:if test="${student.school.archived}">
                    <option value="${student.school.id}" selected="selected">${student.school.name}*</option>
                  </c:if>
                </select>
              </div>
            
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.educationTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.educationHelp"/>
                </jsp:include>                                    
                <input type="text" name="education.${student.id}" value="${fn:escapeXml(student.education)}" size="50">       
              </div>
              
              <div class="genericFormSection">                                    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.previousStudiesTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.previousStudiesHelp"/>
                </jsp:include>
                <input type="text" name="previousStudies.${student.id}" value="${fn:escapeXml(student.previousStudies)}" size="5">
              </div>
  
              <div class="genericFormSection">                                      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyTimeEndTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyTimeEndHelp"/>
                </jsp:include>
                <input type="text" name="studyTimeEnd.${student.id}" class="ixDateField" value="${fn:escapeXml(student.studyTimeEnd.time)}"/>
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyStartDateTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyStartDateHelp"/>
                </jsp:include>
                <input type="text" name="studyStartDate.${student.id}" class="ixDateField" value="${fn:escapeXml(student.studyStartDate.time)}"/>
              </div>
  
              <div class="genericFormSection">    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyEndDateTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyEndDateHelp"/>
                </jsp:include>
                <input type="text" name="studyEndDate.${student.id}" class="ixDateField" value="${fn:escapeXml(student.studyEndDate.time)}"/>
              </div>
  
              <div class="genericFormSection">      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyEndReasonTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyEndReasonHelp"/>
                </jsp:include>
                <select name="studyEndReason.${student.id}">
                  <option></option>  
                  <c:forEach var="reason" items="${studyEndReasons}">
                    <c:choose>
                      <c:when test="${reason.id eq student.studyEndReason.id}">
                        <option value="${reason.id}" selected="selected">${reason.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${reason.id}">${reason.name}</option> 
                      </c:otherwise>
                    </c:choose>
    
                    <c:if test="${fn:length(reason.childEndReasons) gt 0}">
	                    <optgroup>
			                  <c:forEach var="childReason" items="${reason.childEndReasons}">
			                    <c:choose>
			                      <c:when test="${childReason.id eq student.studyEndReason.id}">
			                        <option value="${childReason.id}" selected="selected">${childReason.name}</option> 
			                      </c:when>
			                      <c:otherwise>
			                        <option value="${childReason.id}">${childReason.name}</option> 
			                      </c:otherwise>
			                    </c:choose>
			                  </c:forEach>
                    </optgroup>
                    </c:if>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyEndTextTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyEndTextHelp"/>
                </jsp:include>
              
                <input type="text" name="studyEndText.${student.id}" size="50" value="${fn:escapeXml(student.studyEndText)}">
              </div>
              
              <div class="genericFormSection">    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.lodgingTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.lodgingHelp"/>
                </jsp:include>
                <select name="lodging.${student.id}">
                  <c:choose>
                    <c:when test="${student.lodging}">
                      <option value="0"><fmt:message key="students.editStudent.lodgingNo"/></option>
                      <option value="1" selected="selected"><fmt:message key="students.editStudent.lodgingYes"/></option>
                    </c:when>
                    <c:otherwise>
                      <option value="0" selected="selected"><fmt:message key="students.editStudent.lodgingNo"/></option>
                      <option value="1"><fmt:message key="students.editStudent.lodgingYes"/></option>
                    </c:otherwise>
                  </c:choose>
                </select>
              </div>
  
              <c:choose>
                <c:when test="${fn:length(variableKeys) gt 0}">
                  <div class="genericFormSection">  
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="students.editStudent.variablesTitle"/>
                      <jsp:param name="helpLocale" value="students.editStudent.variablesHelp"/>
                    </jsp:include>
                    <div id="variablesTableContainer.${student.id}"></div>
                  </div>
                </c:when>
              </c:choose>
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.additionalInformationTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.additionalInformationHelp"/>
                </jsp:include>
                <textarea name="additionalInfo.${student.id}" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true">${student.additionalInfo}</textarea>
              </div>
              <ix:extensionHook name="students.editStudent.tabs.studyProgramme"/>
            </div>
          </c:forEach>

          <ix:extensionHook name="students.editStudent.tabs"/>

          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="students.editStudent.saveButton"/>">
          </div>

        </form>

      </div>
    </div>  

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>