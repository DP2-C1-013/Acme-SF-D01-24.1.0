<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="false">
	<acme:input-textbox code="auditor.audit-record.form.label.code" path="code" placeholder="AU-XXXX-XXX"/>
	<acme:input-moment code="auditor.audit-record.form.label.startDate" path="startDate"/>
	<acme:input-moment code="auditor.audit-record.form.label.endDate" path="endDate"/>
	<acme:input-select code="auditor.audit-record.form.label.mark" path="mark" choices="${marks}"/>
	<acme:input-url code="auditor.audit-record.form.label.link" path="link"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish') && draftMode == true && codeAuditDraftMode}">
			<acme:input-textbox code="auditor.audit-record.form.label.code-audit" path="codeAudit" readonly="true"/>		
			<acme:submit code="auditor.audit-record.form.button.delete" action="/auditor/audit-record/delete"/>
			<acme:submit code="auditor.audit-record.form.button.update" action="/auditor/audit-record/update"/>
			<acme:submit code="auditor.audit-record.form.button.publish" action="/auditor/audit-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.audit-record.form.button.create" action="/auditor/audit-record/create?code-auditId=${codeAuditId}"/>
		</jstl:when>
	</jstl:choose>
	
</acme:form>