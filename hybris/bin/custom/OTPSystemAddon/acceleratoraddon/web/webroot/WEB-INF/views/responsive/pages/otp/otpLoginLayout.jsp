<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:theme code="resetPwd.title" var="pageTitle"/>

<template:page pageTitle="${pageTitle}">

			<cms:pageSlot position="otpLogin" var="feature" element="div" class="col-md-3">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	
</template:page>