<%@page language="java"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form>
<jstl:choose>
	<jstl:when test="${command == 'show'}">
	<acme:input-textbox code="inventor.patronage.form.label.status" path="status" readonly="true"/>
	<acme:input-textbox code="inventor.patronage.form.label.code" path="code" readonly="true"/>
	<acme:input-textbox code="inventor.patronage.form.label.legalStuff" path="legalStuff" readonly="true"/>
	<acme:input-money code="inventor.patronage.form.label.budget" path="budget" readonly="true"/>
	<acme:input-textbox code="inventor.patronage.form.label.startsAt" path="startsAt" readonly="true"/>
	<acme:input-textbox code="inventor.patronage.form.label.finishesAt" path="finishesAt" readonly="true"/>
	<acme:input-url code="inventor.patronage.form.label.link" path="link" readonly="true"/>
	<acme:input-textbox code="inventor.patronage.form.label.patronCompany" path="patron.company" readonly="true"/>
	<acme:input-textbox code="inventor.patronage.form.label.patronStatement" path="patron.statement" readonly="true"/>
	<acme:button code="inventor.patronage.form.buttom.patronage-report" action="/inventor/patronage-report/list-by-patronage?id=${id}"/>
		
	<acme:button code="inventor.patronage-report.form.button.create" action="/inventor/patronage-report/create?id=${id}"/>
	</jstl:when>
</jstl:choose>	

</acme:form>