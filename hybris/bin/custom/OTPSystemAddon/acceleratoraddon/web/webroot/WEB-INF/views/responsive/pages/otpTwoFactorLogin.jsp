<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<div class="error"></div>
<div class="success"></div>
<form:form method="POST" commandName="userOtp" id="frm-mobile-verification">
	<div class="form-row">
		<label>Check the OTP in Google Authenticator </label>
	</div>

<form action="${contextPath}/otp/checkOTP" method=GET>
  <label for="otp">Enter OTP</label><br>
  <input type="text" id="otp" name="otp">
  <input type="submit" value="Submit">
</form> 

<img src="${qrCodePath}/Chirag Gupta.png" width="400" height="400" />
