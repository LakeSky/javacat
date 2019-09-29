<%@ page language="java" pageEncoding="UTF-8" %>
<ul class="breadcrumb">
    <c:forEach items="${nodeNav}" var="item" varStatus="status">
        <li>${item}</li>
    </c:forEach>
</ul>