<c:forEach items="${fieldMap['show']}" var="field" varStatus="st">
    <c:set var='fieldname' value="${field.name}" scope="page"/>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right">
                ${field.nickname}
            <c:if test="${false eq field.nullable}"><span style="color: red;">*</span></c:if>:
        </label>
        <div class="col-sm-9">
            <label class="control-label no-padding-right">${obj[fieldname]}</label>
        </div>
    </div>
</c:forEach>