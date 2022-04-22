
<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form readonly="true">
    <acme:input-textbox code="authenticated.patron.patronage.form.label.status" path="status"/>	
	<acme:input-textbox code="authenticated.patron.patronage.form.label.code" path="code"/>	
	<acme:input-textbox code="authenticated.patron.patronage.form.label.legalStuff" path="legalStuff"/>	
	<acme:input-money code="authenticated.patron.patronage.form.label.budget" path="budget"/>	
	<acme:input-textbox code="authenticated.patron.patronage.form.label.startsAt" path="startsAt"/>
	<acme:input-textarea code="authenticated.patron.patronage.form.label.finishesAt" path="finishesAt"/>
	<acme:input-url code="authenticated.patron.patronage.form.label.link" path="link"/>
</acme:form>