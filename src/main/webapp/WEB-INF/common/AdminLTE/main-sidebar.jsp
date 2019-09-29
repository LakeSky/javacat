<%@ page language="java" pageEncoding="UTF-8" %>
<aside class="main-sidebar">
    <section class="sidebar">
        <ul class="sidebar-menu tree" data-widget="tree"><c:set var="parentNode" value="${requestScope.rootNode}" scope="request"/>
            <c:import url="/WEB-INF/common/AdminLTE/treeNode.jsp"/>
            <c:remove var="parentNode" scope="request"/>
        </ul>
    </section>
</aside>