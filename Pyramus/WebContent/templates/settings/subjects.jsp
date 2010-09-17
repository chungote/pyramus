<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="settings.subjects.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>

    <script type="text/javascript">

      var archivedRowIndex;

      function addSubjectsTableRow() {
        var table = getIxTableById('subjectsTable');
        var rowIndex = table.addRow(['', '', '', '', '', -1, 1]);
        for (var i = 0; i < table.getColumnCount(); i++) {
          table.setCellEditable(rowIndex, i, true);
        }
        $('noSubjectsAddedMessageContainer').setStyle({
          display: 'none'
        });
        table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
      }
      
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));

        var subjectsTable = new IxTable($('subjectsTable'), {
          id : "subjectsTable",
          columns : [ {
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="settings.subjects.subjectsTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              for (var i = 0; i < table.getColumnCount(); i++) {
                table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
              }
              table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
            }
          }, {
            header : '<fmt:message key="settings.subjects.subjectsTableCodeHeader"/>',
            left : 38,
            width : 50,
            dataType: 'text',
            editable: false,
            paramName: 'code'
          }, {
            header : '<fmt:message key="settings.subjects.subjectsTableNameHeader"/>',
            left : 96,
            width : 750,
            dataType: 'text',
            editable: false,
            paramName: 'name',
            editorClassNames: 'required'
          }, {
            right: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="settings.subjects.subjectsTableArchiveTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              var subjectId = table.getCellValue(event.row, table.getNamedColumnIndex('subjectId'));
              var subjectName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.subjects.subjectArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(subjectName);

              archivedRowIndex = event.row; 
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="settings.subjects.subjectArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="settings.subjects.subjectArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="settings.subjects.subjectArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("settings/archivesubject.json", {
                      parameters: {
                        subjectId: subjectId
                      },
                      onSuccess: function (jsonResponse) {
                        getIxTableById('subjectsTable').deleteRow(archivedRowIndex);
                        saveFormDraft();
                      }
                    });   
                  break;
                }
              });
            
              dialog.open();
            },
            paramName: 'archiveButton',
            hidden: true
          }, {
            right: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="settings.subjects.subjectsTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableObject.deleteRow(event.row);
              if (event.tableObject.getRowCount() == 0) {
                $('noSubjectsAddedMessageContainer').setStyle({
                  display: ''
                });
              }
            },
            paramName: 'removeButton',
            hidden: true
          }, {
            dataType: 'hidden',
            paramName: 'subjectId'
          }, {
            dataType: 'hidden',
            paramName: 'modified'
          }]
        });

        var rowIndex;
        <c:forEach var="subject" items="${subjects}">
          rowIndex = subjectsTable.addRow([
            '',
            '${fn:replace(subject.code, "'", "\\'")}',
            '${fn:replace(subject.name, "'", "\\'")}',
            '',
            '',
            ${subject.id},
            0
          ]);
          subjectsTable.showCell(rowIndex, subjectsTable.getNamedColumnIndex('archiveButton'));
        </c:forEach>

        if (subjectsTable.getRowCount() > 0) {
          $('noSubjectsAddedMessageContainer').setStyle({
            display: 'none'
          });
        }
      }
        
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="settings.subjects.pageTitle"/></h1>
    
    <div id="manageSubjectsFormContainer"> 
	    <div class="genericFormContainer"> 
	      <form action="savesubjects.json" method="post" ix:jsonform="true" ix:useglasspane="true">
	  
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#manageSubjects">
	            <fmt:message key="settings.subjects.tabLabelSubjects"/>
	          </a>
	        </div>
          
          <div id="manageSubjects" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addSubjectsTableRow();"><fmt:message key="settings.subjects.addSubjectLink"/></span>
            </div>
              
            <div id="noSubjectsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="settings.subjects.noSubjectsAddedPreFix"/> <span onclick="addSubjectsTableRow();" class="genericTableAddRowLink"><fmt:message key="settings.subjects.noSubjectsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="subjectsTable"></div>
          </div>
	  
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="settings.subjects.saveButton"/>">
          </div>

	      </form>
	    </div>
	  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>