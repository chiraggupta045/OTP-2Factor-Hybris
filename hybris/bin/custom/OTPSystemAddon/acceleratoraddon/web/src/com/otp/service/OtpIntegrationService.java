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

	ResponseEntity<OtpResponse> sendOtpForVerification(final OtpRequestPayload requestPayLoad, final RestTemplate restTemplate,
			final HttpHeaders headers);
}
