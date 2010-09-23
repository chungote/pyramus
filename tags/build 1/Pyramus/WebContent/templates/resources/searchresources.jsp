<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
	  <title><fmt:message key="resources.searchResources.pageTitle"/></title>

	  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
	  
	  <script type="text/javascript">
	    function doSearch(page) {
	      var searchForm = $("searchForm");
	      JSONRequest.request("resources/searchresources.json", {
	        parameters: {
	          name: searchForm.name.value,
	          resourceType: searchForm.resourceType.value,
	          resourceCategory: searchForm.resourceCategory.value,
	          page: page
	        },
	        onSuccess: function (jsonResponse) {
	          var resultsTable = getIxTableById('resourcesTable');
	          resultsTable.deleteAllRows();
	          var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var resourceType;        
              if (results[i].resourceType == 'MATERIAL_RESOURCE') {
                resourceType = '<fmt:message key="resources.searchResources.resourceType_MATERIAL_RESOURCE"/>';
              }
              else if (results[i].resourceType == 'WORK_RESOURCE') { 
                resourceType = '<fmt:message key="resources.searchResources.resourceType_WORK_RESOURCE"/>';
              }
              resultsTable.addRow([results[i].name, resourceType, results[i].resourceCategoryName, '', '', results[i].resourceType, results[i].id]);
            }
            getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
            $('searchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
            $('searchResultsWrapper').setStyle({
              display: ''
            });
	        } 
	      });
	    }
	
	    function onSearchResources(event) {
	      Event.stop(event);
	      doSearch(0);
	    }

	    function onLoad(event) {
	      var tabControl = new IxProtoTabs($('tabs'));
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(event.page);
          }
        });
        var resultsTable = new IxTable($('searchResultsTableContainer'), {
          id : "resourcesTable",
          columns : [ {
            header : '<fmt:message key="resources.searchResources.resourcesTableNameHeader"/>',
            left: 8,
            right: 476,
            dataType: 'text',
            editable: false,
            paramName: 'name'
          }, {
            header : '<fmt:message key="resources.searchResources.resourcesTypeNameHeader"/>',
            right: 268,
            width : 200,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="resources.searchResources.resourcesCategoryNameHeader"/>',
            width: 200,
            right : 60,
            dataType: 'text',
            editable: false
          }, {
            width: 30,
            right : 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="resources.searchResources.resourceTableEditResourceTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              var resourceId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceId'));
              var resourceType = table.getCellValue(event.row, table.getNamedColumnIndex('resourceType'));
              if (resourceType == 'MATERIAL_RESOURCE') {
                redirectTo(GLOBAL_contextPath + '/resources/editmaterialresource.page?resource=' + resourceId);
              } else {
                redirectTo(GLOBAL_contextPath + '/resources/editworkresource.page?resource=' + resourceId);
              }
            } 
          }, {
            width: 30,
            right : 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="resources.searchResources.resourceTableArchiveResourceTooltip"/>',
            onclick: function (event) {
              var table = event.tableObject;
              var resourceId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceId'));
              var resourceName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=resources.searchResources.resourceArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(resourceName);
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="resources.searchResources.resourceArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="resources.searchResources.resourceArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="resources.searchResources.resourceArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener( function(event) {
                var dlg = event.dialog;
            
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("resources/archiveresource.json", {
                      parameters: {
                        resource: resourceId
                      },
                      onSuccess: function (jsonResponse) {
                        var currentPage = getSearchNavigationById('searchResultsNavigation').getCurrentPage();
                        doSearch(currentPage);
                      }
                    });   
                  break;
                }
              });
            
              dialog.open(); 
            }
          }, {
            dataType: 'hidden',
            paramName: 'resourceType'
          }, {
            dataType: 'hidden',
            paramName: 'resourceId'
          }]
        });
	    };
    </script>
  
  </head> 
  <body onload="onLoad(event);">
	  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
	  
	  <h1 class="genericPageHeader"><fmt:message key="resources.searchResources.pageTitle" /></h1>
	  
	  <div id="searchResourcesSearchFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#searchResources">
            <fmt:message key="resources.searchResources.tabLabelSearchResources"/>
          </a>
        </div>
        
        <div id="searchResources" class="tabContent">
          <form id="searchForm" method="post" onsubmit="onSearchResources(event);">
      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.nameTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.nameHelp"/>
              </jsp:include>          
              <input type="text" name="name" size="40"/>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.resourceTypeTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.resourceTypeHelp"/>
              </jsp:include>          
              <div class="searchResourcesResourceTypeContainer">
                <select name="resourceType">
                  <option></option>
                  <c:forEach var="resourceType" items="${resourceTypes}">
                    <option value="${resourceType}"><fmt:message key="resources.searchResources.resourceType_${resourceType}"/></option>
                  </c:forEach>
                </select>
              </div>
            </div>
      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.resourceCategoryTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.resourceCategoryHelp"/>
              </jsp:include>          
              <div class="searchResourcesResourceCategoryContainer">
                <select name="resourceCategory">
                  <option></option>
                  <c:forEach var="resourceCategory" items="${resourceCategories}">
                    <option value="${resourceCategory.id}">${resourceCategory.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
      
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="resources.searchResources.searchButton"/>">
            </div>
      
          </form>
		</div>
	  </div>
	</div>
	     
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="resources.searchResources.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
	  
	  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
	</body>
</html>

