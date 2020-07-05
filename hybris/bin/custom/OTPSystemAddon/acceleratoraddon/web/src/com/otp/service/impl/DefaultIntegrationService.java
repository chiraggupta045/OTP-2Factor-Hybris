/**
 *
 */
package com.otp.service.impl;

import de.hybris.platform.util.Config;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.otp.OtpRequestPayload;
import com.otp.response.pojo.OtpResponse;
import com.otp.service.OtpIntegrationService;


@Component
public class DefaultIntegrationService implements OtpIntegrationService
{
	public static String OTP_URL = "com.hpe.otp.url";

	@Override
	public ResponseEntity<OtpResponse> sendOtpForVerification(final OtpRequestPayload requestPayLoad,
			final RestTemplate restTemplate, final HttpHeaders headers)
	{
		final HttpEntity<OtpRequestPayload> entityForPostCall = new HttpEntity<>(requestPayLoad, headers);
		return restTemplate.exchange(Config.getParameter(OTP_URL), HttpMethod.POST, entityForPostCall, OtpResponse.class);
	}
}