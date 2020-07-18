<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
h4{
	font-family: 'Raleway', sans-serif;
	font-size: 	25px;
	padding-top: 200px;
	/*padding-bottom: 10px;*/
	/*border-bottom: 1px solid #757575; */
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
 <c:when test="${!isUserEnabledFor2FactorAuth}">
        <h4>Please Scan the QR code displayed below from the <br>
        Google Authenticator Application in your Mobile <br>
        and Enter the six digit OTP Code</h4>
 </c:when>
 <c:otherwise>
    <h4>Please Enter the OTP from <br>Google Authenticator Application in your Mobile</h4>
 </c:otherwise>
</c:choose>
  <div class='container'>
    <form:form action="${contextPath}/otp/checkOTP" method="post" commandName="otpFormData">
        <formElement:formPasswordBox idKey="otp"
                                     labelKey="" path="otp" inputCSS="form-control"
                                     mandatory="true" />
        <br>
        <button type="submit">Submit</button>
    </form:form>
    <c:choose>
     <c:when test="${!isUserEnabledFor2FactorAuth}">
           <img src="${commonResourcePath}/images/QRImages/${userName}.png" alt="image"/>
     </c:when>
     <c:otherwise>
        <h3>Note: Logged-In User is already Enabled For 2-Factor Authentication.</h3>
     </c:otherwise>
    </c:choose>
  </div>
</body>