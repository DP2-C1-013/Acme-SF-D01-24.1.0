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
	<acme:input-textbox code="sponsor.invoice.form.label.code" path="code" placeholder="IN-XXXX-XXXX"/>
	<jstl:if test="${acme:anyOf(_command,'show|update|delete|publish')}">	
		<acme:input-moment code="sponsor.invoice.form.label.registrationTime" path="registrationTime" readonly="true"/>
	</jstl:if>
	<acme:input-moment code="sponsor.invoice.form.label.dueDate" path="dueDate"/>
	<acme:input-money code="sponsor.invoice.form.label.quantity" path="quantity"/>
	<acme:input-double code="sponsor.invoice.form.label.tax" path="tax" placeholder="0.21"/>
	<jstl:if test="${acme:anyOf(_command,'show|update|delete|publish')}">
		<acme:input-money code="sponsor.invoice.form.label.totalAmount" path="totalAmount" readonly="true"/>
	</jstl:if>
	<acme:input-url code="sponsor.invoice.form.label.link" path="link"/>
	<acme:input-textbox code="sponsor.invoice.form.label.sponsorship" path="sponsorship" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish') && draftMode == true && sponsorshipDraftMode}">
			<acme:submit code="sponsor.invoice.form.button.delete" action="/sponsor/invoice/delete"/>
			<acme:submit code="sponsor.invoice.form.button.update" action="/sponsor/invoice/update"/>
			<acme:submit code="sponsor.invoice.form.button.publish" action="/sponsor/invoice/publish"/>		
		</jstl:when>
		<jstl:when test="${_command == 'create' && sponsorshipDraftMode}">
			<acme:submit code="sponsor.invoice.form.button.create" action="/sponsor/invoice/create?sponsorshipId=${sponsorshipId}"/>
		</jstl:when>
	</jstl:choose>
	
</acme:form>