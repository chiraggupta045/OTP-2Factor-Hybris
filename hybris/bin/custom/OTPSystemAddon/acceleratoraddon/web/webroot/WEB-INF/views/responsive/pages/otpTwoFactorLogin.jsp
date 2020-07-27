<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style>
h4{
	font-family: 'Raleway', sans-serif;
	font-size: 	25px;
	padding-top: 200px;
	text-align: center;
    color: #f37121;
}
.container {
	font-family: 'Raleway', sans-serif;
	font-size: 20px;
	text-align: center;
}
</style>

<body>
<c:choose>
 <c:when test="${smsBasedOTP}">
 <h4><spring:message code="text.otp.user.sms.message" text="PLease Enter the OTP received in your mobile through SMS"/></h4>
  <div class='container'>
      <form:form action="${contextPath}/otp/validateOTP" method="post" commandName="otpFormData">
          <formElement:formPasswordBox idKey="otp"
                                       labelKey="" path="otp" inputCSS="form-control"
                                       mandatory="true" />
          <br>
          <button type="submit">Submit</button>
      </form:form>
      <h3><spring:message code="text.otp.user.enabled.sms.status" text="Note: Logged-In User is already Enabled For 2-Factor Authentication."/></h3>
  </div>
 </c:when>

 <c:otherwise>
     <c:choose>
      <c:when test="${!isUserAlreadyEnabledFor2FactorAuth}">
          <h4><spring:message code="text.otp.user.new.message" text="Please Scan the QR Code Below"/></h4>
      </c:when>
      <c:otherwise>
          <h4><spring:message code="text.otp.user.enabled.message" text="Please Check the OTP from Phone App"/></h4>
      </c:otherwise>
     </c:choose>
 <div class='container'>
         <form:form action="${contextPath}/otp/validateOTP" method="post" commandName="otpFormData">
             <formElement:formPasswordBox idKey="otp"
                                          labelKey="" path="otp" inputCSS="form-control"
                                          mandatory="true" />
             <br>
             <button type="submit">Submit</button>
         </form:form>
     <c:choose>
          <c:when test="${!isUserAlreadyEnabledFor2FactorAuth}">
                <img src="${commonResourcePath}/images/QRImages/${userName}.png" alt="QR Code Image"/>
          </c:when>
          <c:otherwise>
             <h3><spring:message code="text.otp.user.enabled.status" text="Note: Logged-In User is already Enabled For 2-Factor Authentication."/></h3>
          </c:otherwise>
     </c:choose>
  </div>
 </c:otherwise>
</c:choose>
</body>