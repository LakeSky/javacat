<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:forEach var="childNode" items="${requestScope.parentNode.children}">
    <c:if test="${not empty childNode.children}">
        <li class="treeview <c:if test="${childNode.checked}">menu-open active</c:if>">
            <a href="javascript:void(0);">
                <i class="${childNode.icon}"></i>
                <span> ${childNode.name} </span>
                <span class="pull-right-container">
                     <i class="fa fa-angle-left pull-right"></i>
                </span>
            </a>
            <ul class="treeview-menu" style="<c:if test="${childNode.checked}">display: block</c:if>">
                <c:forEach items="${childNode.children}" var="item" varStatus="status">
                    <li <c:if test="${item.checked}">class="active"</c:if>>
                        <a href="${contextPath}${item.url}">
                            <i class="${item.icon}"></i>
                            <span> ${item.name} </span>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </li>
    </c:if>
    <c:if test="${empty childNode.children}">
        <li <c:if test="${childNode.checked}">class="active"</c:if>>
            <a href="${contextPath}${childNode.url}">
                <i class="${childNode.icon}"></i>
                <span> ${childNode.name} </span>
            </a>
        </li>
    </c:if>
</c:forEach>
