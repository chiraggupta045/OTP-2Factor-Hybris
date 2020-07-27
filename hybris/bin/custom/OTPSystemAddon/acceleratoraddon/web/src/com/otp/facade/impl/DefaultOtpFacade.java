/**
 *
 */
package com.otp.facade.impl;

import com.otp.constants.OTPSystemAddonFacadeConstants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.css.Counter;

import com.otp.OtpRequestPayload;
import com.otp.facade.OtpFacade;
import com.otp.response.pojo.OtpResponse;
import com.otp.service.OtpIntegrationService;
import com.otp.service.otpgenerator.OTP;
import com.otp.service.otpgenerator.OTPEngine;

import java.security.NoSuchAlgorithmException;

@Component
public class DefaultOtpFacade implements OtpFacade
{
	/** OTP Integration Service */
	@Autowired
	private OtpIntegrationService otpIntegrationService;

	/** Configuration Service */
	@Autowired
	private ConfigurationService configurationService;

	/** Session Service */
	@Autowired
	private SessionService sessionService;

	/** User Service */
	@Autowired
	private UserService userService;

	/** Logger Instance for DefaultOtpFacade */
	private static final Logger LOG = Logger.getLogger(DefaultOtpFacade.class);

	/**
	 * send otp for verification
	 * @param otp
	 * @return
	 */
	@Override
	public ResponseEntity<OtpResponse> sendOtpForVerification(final String otp)
	{
		final RestTemplate restTemplate = new RestTemplate();
		// create the otp request payload
		final OtpRequestPayload otpRequestPayload = getOtpRequestPayload("9821946885", otp);
		final HttpHeaders headers = new HttpHeaders();
		headers.set(OTPSystemAddonFacadeConstants.AUTHORIZATION, (String) configurationService.getConfiguration().getProperty(OTPSystemAddonFacadeConstants.OTP_KEY));
		final ResponseEntity<OtpResponse> entity = otpIntegrationService.sendOtpForVerification(otpRequestPayload, restTemplate,
				headers);
		return entity;
	}

	/**
	 * generate Otp for authentication
	 *
	 * @return
	 */
	@Override
	public OTP generateOtpForAuthentication()
	{
		String customerId = userService.getCurrentUser().getUid();
		SecretKey secretKey = generateSessionKey();
		final OTPEngine otpEngine = OTPEngine.getInstance(secretKey, new Counter()
		{
			@Override
			public String getIdentifier()
			{
				return OTPSystemAddonFacadeConstants.OTP;
			}

			@Override
			public String getListStyle()
			{
				return OTPSystemAddonFacadeConstants.LIST_STYLE;
			}

			@Override
			public String getSeparator()
			{
				return OTPSystemAddonFacadeConstants.SEPARATOR;
			}
		});
		final String[] params = new String[]
		{ customerId };
		return otpEngine.generatePasswordWithHmac(params);
	}

	private SecretKey generateSessionKey()
	{
		KeyGenerator keyGen = null;
		try
		{
			keyGen = KeyGenerator.getInstance(OTPSystemAddonFacadeConstants.KEY_GEN_VALUE);
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.debug("NoSuchAlgorithmException"+e.getMessage());
		}
		if (keyGen != null) {
			keyGen.init(OTPSystemAddonFacadeConstants.KEY_GEN_INTEGER);
			return keyGen.generateKey();
		}
		return null;
	}

	/**
	 * the method will verify otp
	 * @param otp
	 * @return
	 */
	@Override
	public boolean validateSMSBasedOtp(final String otp)
	{
		final String userOtp = sessionService.getAttribute(OTPSystemAddonFacadeConstants.OTP);
		return StringUtils.equals(otp, userOtp);
	}

	/**
	 * create the otp request payload
	 * @param number
	 * @param otp
	 * @return
	 */
	private OtpRequestPayload getOtpRequestPayload(String number, String otp) {
		final OtpRequestPayload otpRequestPayload = new OtpRequestPayload();
		otpRequestPayload.setLanguage((String) configurationService.getConfiguration().getProperty(OTPSystemAddonFacadeConstants.LANGUAGE));
		otpRequestPayload.setSender_id((String) configurationService.getConfiguration().getProperty(OTPSystemAddonFacadeConstants.SENDER_ID));
		otpRequestPayload.setNumbers(number);
		otpRequestPayload.setMessage((String) configurationService.getConfiguration().getProperty(OTPSystemAddonFacadeConstants.TEMPLATE_ID));
		otpRequestPayload.setRoute((String) configurationService.getConfiguration().getProperty(OTPSystemAddonFacadeConstants.ROUTE_ID));
		otpRequestPayload.setVariables((String) configurationService.getConfiguration().getProperty(OTPSystemAddonFacadeConstants.OTP_VARIABLE));
		otpRequestPayload.setVariables("{#BB#}");
		otpRequestPayload.setVariables_values(otp);
		return otpRequestPayload;
	}
}