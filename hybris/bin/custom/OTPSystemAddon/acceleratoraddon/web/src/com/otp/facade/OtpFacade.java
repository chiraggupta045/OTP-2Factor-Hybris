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
	 * @param number
	 * @param otp
	 * @return
	 */
	ResponseEntity<OtpResponse> sendOtpForVerification(final String number, final String otp);

	/**
	 * generate otp for authentication
	 *
	 * @param customerId
	 * @param secretKey
	 * @return
	 */
	OTP generateOtpForAuthentication(final String customerId, final SecretKey secretKey);

	boolean verifyingOtp(final String otp);
}
