<%@ page language="java" pageEncoding="UTF-8" %>
<c:forEach items="${fieldMap['query']}" var="field" varStatus="st">
    <c:choose>
        <c:when test="${'text' eq field.type}">
            <div class="form-group">
                <input type="text" name="${field.name}" value="${conditions.plateNumber}" class="form-control" placeholder="${field.nickname}">
            </div>
        </c:when>
        <c:when test="${'textarea' eq field.type}">
            <textarea type="text" name="${field.name}" class="form-control" placeholder="${field.nickname}">
                    ${conditions.plateNumber}
            </textarea>
        </c:when>
        <c:when test="${'dict' eq field.type}">
            <select qType="dict" name="${field.name}" class="form-control">
                <option value="">${field.nickname}</option>
                <c:forEach items="${field.dictMap}" var="item">
                    <option value="${item.key}">${item.value}</option>
                </c:forEach>
            </select>
        </c:when>
        <c:when test="${'date' eq field.type}">
            <c:choose>
                <c:when test="${'yyyy-MM-dd' eq field.format}">
                    <input class="date-picker form-control"
                           id="${field.name}_start" type="text" name="${field.name}_start" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'${field.name}_end\')}'})" placeholder="${field.nickname}开始时间"/>
                    -
                    <input class="date-picker form-control"
                           id="${field.name}_end" type="text" name="${field.name}_end" onclick="WdatePicker({minDate:'#F{$dp.$D(\'${field.name}_start\')}'})" placeholder="${field.nickname}结束时间"/>
                </c:when>
                <c:when test="${'yyyy-MM-dd HH:mm:ss' eq field.format}">
                    <label>${field.nickname}:</label>
                    <input type="text" name="${field.name}_start" class="form-control"/>
                    - <input type="text" name="${field.name}_end" class="form-control"/>
                </c:when>
            </c:choose>
        </c:when>
    </c:choose>
</c:forEach>