<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>APPID:</label> <input name="bunlderId" type="text" size="30" value="${entity.bunlderId}" /> </p>
<c:set var="value" value="${entity.sandbox}" />
<p> <label>沙盒测试:</label> <input name="sandbox" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="sandbox" value="false" />
<div class="divider"></div>
	</div>