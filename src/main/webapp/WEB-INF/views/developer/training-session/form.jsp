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
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
			<acme:input-textbox code="developer.training-session.form.label.code" path="code" placeholder="TS-XXX-123" readonly="true"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-textbox code="developer.training-session.form.label.code" path="code" placeholder="TS-XXX-123"/>
		</jstl:when>
	</jstl:choose>
	
	<acme:input-moment code="developer.training-session.form.label.startDate" path="startDate"/>
	<acme:input-moment code="developer.training-session.form.label.endDate" path="endDate"/>
	<acme:input-textbox code="developer.training-session.form.label.location" path="location"/>
	<acme:input-textbox code="developer.training-session.form.label.instructor" path="instructor"/>
	<acme:input-email code="developer.training-session.form.label.contactEmail" path="contactEmail"/>
	<acme:input-url code="developer.training-session.form.label.optionalLink" path="optionalLink"/>
	<acme:input-checkbox code="developer.training-session.form.label.draftMode" path="draftMode" readonly="true"/>
	<acme:input-textbox code="developer.training-session.form.label.trainingModuleCode" path="trainingModuleCode" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && trainingModuleNotPublished == true && draftMode == true}">
			<acme:submit code="developer.training-session.form.button.update" action="/developer/training-session/update"/>
			<acme:submit code="developer.training-session.form.button.delete" action="/developer/training-session/delete"/>
			<acme:submit code="developer.training-session.form.button.publish" action="/developer/training-session/publish"/>

		</jstl:when>
		<jstl:when test="${_command == 'create' && trainingModuleNotPublished == true}">
			<acme:submit code="developer.training-session.form.button.create" action="/developer/training-session/create?trainingModuleId=${trainingModuleId}"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>
