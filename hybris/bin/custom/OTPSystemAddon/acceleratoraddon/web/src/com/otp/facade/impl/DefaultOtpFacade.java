/**
 *
 */
package com.otp.facade.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.css.Counter;

import com.otp.OtpRequestPayload;
import com.otp.facade.OtpFacade;
import com.otp.response.pojo.OtpResponse;
import com.otp.service.OtpIntegrationService;
import com.otp.service.otpgenerator.OTP;
import com.otp.service.otpgenerator.OTPEngine;


public class DefaultOtpFacade implements OtpFacade
{
	@Autowired
	private OtpIntegrationService otpIntegrationService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private SessionService sessionService;

	public static String LANGUAGE = "com.otp.language";
	public static String SENDER_Id = "com.otp.sender_id";
	public static String TEMPLATE_ID = "com.otp.template_id";
	public static String ROUTE_ID = "com.otp.route_id";
	public static String OTP_KEY = "com.otp.appKey";
	public static String OTP_VARIABLE = "com.otp.variable";

	/**
	 * send otp for verification
	 *
	 * @param number
	 * @param otp
	 * @return
	 */
	@Override
	public ResponseEntity<OtpResponse> sendOtpForVerification(final String number, final String otp)
	{
		final RestTemplate restTemplate = new RestTemplate();
		final OtpRequestPayload otpRequestPayload = new OtpRequestPayload();


		otpRequestPayload.setLanguage((String) configurationService.getConfiguration().getProperty(LANGUAGE));
		otpRequestPayload.setSender_id((String) configurationService.getConfiguration().getProperty(SENDER_Id));
		otpRequestPayload.setNumbers(number);
		otpRequestPayload.setMessage((String) configurationService.getConfiguration().getProperty(TEMPLATE_ID));
		otpRequestPayload.setRoute((String) configurationService.getConfiguration().getProperty(ROUTE_ID));
		otpRequestPayload.setVariables((String) configurationService.getConfiguration().getProperty(OTP_VARIABLE));
		otpRequestPayload.setVariables("{#BB#}");
		otpRequestPayload.setVariables_values(otp);
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", (String) configurationService.getConfiguration().getProperty(OTP_KEY));

		final ResponseEntity<OtpResponse> entity = otpIntegrationService.sendOtpForVerification(otpRequestPayload, restTemplate,
				headers);
		return entity;
	}

	/**
	 * generate Otp for authentication
	 *
	 * @param customerId
	 * @param secretKey
	 * @return
	 */
	@Override
	public OTP generateOtpForAuthentication(final String customerId, final SecretKey secretKey)
	{

		final OTPEngine otpEngine = OTPEngine.getInstance(secretKey, new Counter()
		{
			@Override
			public String getIdentifier()
			{
				return "OTP";
			}

			@Override
			public String getListStyle()
			{
				return "A";
			}

			@Override
			public String getSeparator()
			{
				return "_";
			}
		});
		final String params[] = new String[]
		{ customerId };
		return otpEngine.generatePasswordWithHmac(params);
	}

	@Override
	public boolean verifyingOtp(final String otp)
	{
		final String userOtp = sessionService.getAttribute("otp");
		if (StringUtils.equals(otp, userOtp))
		{

			return true;
		}
		else
		{
			return false;
		}
	}
}
