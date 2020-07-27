/**
 *
 */
package com.otp.service;

import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.otp.OtpRequestPayload;
import com.otp.response.pojo.OtpResponse;

public interface OtpIntegrationService
{

	/**
	 * This method will generate Qr Code on the basis of secret key
	 * @param secretKey
	 * @param userService
	 */
	void getGoogleAuthenticatorBarCode(final String secretKey, UserService userService);


	/**
	 * This method will send otp to registered moblie no.
	 * @param requestPayLoad
	 * @param restTemplate
	 * @param headers
	 * @return
	 */
	ResponseEntity<OtpResponse> sendOtpForVerification(final OtpRequestPayload requestPayLoad, final RestTemplate restTemplate,
			final HttpHeaders headers);
}
