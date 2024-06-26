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
			<acme:input-textbox code="developer.training-module.form.label.code" path="code" placeholder="XXX-123" readonly="true"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-textbox code="developer.training-module.form.label.code" path="code" placeholder="XXX-123"/>
		</jstl:when>
	</jstl:choose>
	<acme:input-moment code="developer.training-module.form.label.creationMoment" path="creationMoment" />
	<acme:input-textarea code="developer.training-module.form.label.details" path="details" />
	<acme:input-select code="developer.training-module.form.label.difficultyLevel" path="difficultyLevel" choices="${difficultyLevels}" />
	<acme:input-moment code="developer.training-module.form.label.updateMoment" path="updateMoment" />
	<acme:input-url code="developer.training-module.form.label.optionalLink" path="optionalLink" />
	<acme:input-checkbox code="developer.training-module.form.label.draftMode" path="draftMode" readonly="true"/>
	<acme:input-select code="developer.training-module.form.label.project" path="project" choices="${projects}" />
	<acme:input-integer code="developer.training-module.form.label.estimatedTotalTime" path="estimatedTotalTime" readonly="true"/>
	<acme:button code="developer.training-module.form.button.training-sessions" action="/developer/training-session/list?trainingModuleId=${id}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="developer.training-module.form.button.delete" action="/developer/training-module/delete"/>
			<acme:submit code="developer.training-module.form.button.update" action="/developer/training-module/update" />
			<acme:submit code="developer.training-module.form.button.publish" action="/developer/training-module/publish"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="developer.training-module.list.button.create" action="/developer/training-module/create" />
		</jstl:when>
	</jstl:choose>
</acme:form>