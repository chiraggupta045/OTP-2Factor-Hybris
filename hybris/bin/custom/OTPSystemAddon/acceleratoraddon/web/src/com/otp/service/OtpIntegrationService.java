/**
 *
 */
package com.otp.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.otp.OtpRequestPayload;
import com.otp.response.pojo.OtpResponse;



public interface OtpIntegrationService
{
	/**
	 * this method will send otp to registered moblie no.
	 * @param requestPayLoad
	 * @param restTemplate
	 * @param headers
	 * @return
	 */
	ResponseEntity<OtpResponse> sendOtpForVerification(final OtpRequestPayload requestPayLoad, final RestTemplate restTemplate,
			final HttpHeaders headers);
}
