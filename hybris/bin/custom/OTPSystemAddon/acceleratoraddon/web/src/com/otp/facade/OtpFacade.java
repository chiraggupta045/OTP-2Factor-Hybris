/**
 *
 */
package com.otp.facade;

import javax.crypto.SecretKey;

import org.springframework.http.ResponseEntity;

import com.otp.response.pojo.OtpResponse;
import com.otp.service.otpgenerator.OTP;



public interface OtpFacade
{
	/**
	 * send otp for vefication
	 *
	 * @param otp
	 * @return
	 */
	ResponseEntity<OtpResponse> sendOtpForVerification(final String otp);

	/**
	 *
	 *  generate otp for authentication
	 * @return
	 */

	OTP generateOtpForAuthentication();

	/**
	 *
	 * @param otp
	 * @return
	 */
	boolean validateSMSBasedOtp(final String otp);
}
