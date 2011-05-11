<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="settings.courseDescriptionCategories.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
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

      function addRow() {
        var table = getIxTableById('courseDescriptionCategoriesTable');
        var rowIndex = table.addRow(['', '', -1]);
        for (var i = 0; i < table.getColumnCount(); i++) {
          table.setCellEditable(rowIndex, i, true);
        }
        
        $('noneAddedMessageContainer').setStyle({
          display: 'none'
        });
      }
      
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));

        var courseDescriptionCategoriesTable = new IxTable($('courseDescriptionCategoriesTable'), {
          id : "courseDescriptionCategoriesTable",
          columns : [ {
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="settings.courseDescriptionCategories.courseDescriptionCategoriesTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              for (var i = 0; i < table.getColumnCount(); i++) {
                table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
              }
            }
          }, {
            header : '<fmt:message key="settings.courseDescriptionCategories.categoryNameTableHeader"/>',
            left: 234,
            right: 46,
            dataType: 'text',
            editable: false,
            paramName: 'name',
            required: true
          }, {
            dataType: 'hidden',
            paramName: 'categoryId'
          }]
        });
        
        var rowIndex;
        courseDescriptionCategoriesTable.detachFromDom();
        <c:forEach var="category" items="${courseDescriptionCategories}">
          rowIndex = courseDescriptionCategoriesTable.addRow([
            '',
            '${fn:escapeXml(category.name)}',
            ${category.id}
          ]);
        </c:forEach>
        courseDescriptionCategoriesTable.reattachToDom();

        if (courseDescriptionCategoriesTable.getRowCount() > 0) {
          $('noneAddedMessageContainer').setStyle({
            display: 'none'
          });
        }
      }
        
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="settings.courseDescriptionCategories.pageTitle"/></h1>
    
    <div id="manageCourseDescriptionCategoriesFormContainer"> 
	    <div class="genericFormContainer"> 
	      <form action="savecoursedescriptioncategories.json" method="post" ix:jsonform="true" ix:useglasspane="true">
	  
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#manageCourseDescriptionCategories">
	            <fmt:message key="settings.courseDescriptionCategories.tabLabelCourseDescriptionCategories"/>
	          </a>
	        </div>
          
          <div id="manageCourseDescriptionCategories" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addRow();"><fmt:message key="settings.courseDescriptionCategories.addCategoryLink"/></span>
            </div>
              
            <div id="noneAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="settings.courseDescriptionCategories.noCategoriesAddedPreFix"/> <span onclick="addRow();" class="genericTableAddRowLink"><fmt:message key="settings.courseDescriptionCategories.noCategoriesAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="courseDescriptionCategoriesTable"></div>
          </div>
	  
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="settings.courseDescriptionCategories.saveButton"/>">
          </div>

	      </form>
	    </div>
	  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>