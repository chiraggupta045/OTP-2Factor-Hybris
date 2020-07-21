/**
 *
 */
package com.otp.service.impl;


import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	ConfigurationService configurationService;
	public static String OTP_URL = "com.hpe.otp.url";
	private static final Logger LOG = Logger.getLogger(DefaultIntegrationService.class);

	/**
	 * this method is to send the otp to registered mobile no
	 * @param requestPayLoad
	 * @param restTemplate
	 * @param headers
	 * @return
	 */
	@Override
	public ResponseEntity<OtpResponse> sendOtpForVerification(final OtpRequestPayload requestPayLoad,
			final RestTemplate restTemplate, final HttpHeaders headers)
	{
		LOG.debug("sending the otp to registered no");
		final HttpEntity<OtpRequestPayload> entityForPostCall = new HttpEntity<>(requestPayLoad, headers);
		return restTemplate.exchange(configurationService.getConfiguration().getString(OTP_URL), HttpMethod.POST, entityForPostCall, OtpResponse.class);
	}
}