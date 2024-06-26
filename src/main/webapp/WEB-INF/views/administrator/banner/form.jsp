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
	<jstl:if test="${acme:anyOf(_command,'show|update|delete|publish')}">
		<acme:input-moment code="administrator.banner.form.label.moment" path="instantiationMoment" readonly="true"/>
	</jstl:if>
	<acme:input-moment code="administrator.banner.form.label.startDisplay" path="startDisplay"/>
	<acme:input-moment code="administrator.banner.form.label.endDisplay" path="endDisplay"/>
	<acme:input-url code="administrator.banner.form.label.picture" path="picture"/>
	<acme:input-textbox code="administrator.banner.form.label.slogan" path="slogan"/>
	<acme:input-url code="administrator.banner.form.label.link" path="link"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' || _command == 'update'|| _command == 'delete'}">
			<acme:submit code="administrator.banner.list.button.update" action="/administrator/banner/update"/>
			<acme:submit code="administrator.banner.list.button.delete" action="/administrator/banner/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.banner.list.button.create" action="/administrator/banner/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>	