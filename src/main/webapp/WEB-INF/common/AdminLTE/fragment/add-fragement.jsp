<c:forEach items="${fieldMap['edit']}" var="field" varStatus="st">
    <div class="form-group">
        <label class="col-xs-3 control-label no-padding-right">
                ${field.nickname}
            <c:if test="${false eq field.nullable}"><span style="color: red;">*</span></c:if>
        </label>
        <div class="col-xs-9">
            <c:choose>
                <c:when test="${'text' eq field.type}">
                    <input type="text" name="${field.name}" class="col-xs-10 col-sm-5">
                </c:when>
                <c:when test="${'textarea' eq field.type}">
                    <textarea class="col-xs-10 col-sm-5" name="${field.name}"></textarea>
                </c:when>
                <c:when test="${'dict' eq field.type}">
                    <select qType="dict" name="${field.name}" class="col-xs-10 col-sm-5" data-options="<c:if test="${false eq field.nullable}">required:true</c:if>">
                        <option value=""></option>
                        <c:forEach items="${field.dictMap}" var="item">
                            <option value="${item.key}">${item.value}</option>
                        </c:forEach>
                    </select>
                </c:when>
                <c:when test="${'date' eq field.type}">
                    <c:choose>
                        <c:when test="${'yyyy-MM-dd' eq field.format}">
                            <input class="date-picker col-xs-10 col-sm-5" type="text" name="${field.name}" onclick="WdatePicker()"/><i class="ace-icon fa fa-calendar fa-lg"></i>
                        </c:when>
                        <c:when test="${'yyyy-MM-dd HH:mm:ss' eq field.format}">
                            <input class="date-picker col-xs-10 col-sm-5" type="text" name="${field.name}" onclick="WdatePicker()"/><i class="ace-icon fa fa-calendar fa-lg"></i>
                        </c:when>
                    </c:choose>
                </c:when>
            </c:choose>
        </div>
    </div>
</c:forEach>