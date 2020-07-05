/**
 *
 */
package com.otp.controller;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.otp.ControllerConstants;
import com.otp.facade.SecretKeyFacade;


/**
 * @author chiragupta
 *
 */

@Controller
@RequestMapping(value = "/otp")
public class OTPController extends AbstractPageController
{

	private static final Logger LOG = Logger.getLogger(OTPController.class);

	@Autowired
	private SecretKeyFacade secretKeyFacade;

	@Autowired
	private ConfigurationService configurationService;

	private static final String REDIRECT_HOME_URL = REDIRECT_PREFIX + "/";
	private static final String REDIRECT_LOGIN_URL = REDIRECT_PREFIX + "/login";

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String getOTP(final Model model)
	{
		LOG.info("Otp Controller to verify whether user is 2-factor enabled or First time logged in user");
		try
		{
			final String userAuthStatus = secretKeyFacade.checkUserAuthentication();
			LOG.info(userAuthStatus);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		model.addAttribute("qrCodePath", configurationService.getConfiguration().getString("otp.qr.code.images"));
		return ControllerConstants.Actions.Pages.Account.OTP;
	}

	/**
	 *
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkOTP", method = RequestMethod.GET)
	public String checkOTP(@RequestParam(name = "otp") final String otp) throws Exception
	{
		LOG.info("OTP Controller to check the OTP authenticity by comparing it with Google Authenticator generated OTP");

		final boolean success = secretKeyFacade.validateCodeTypedByUser(otp);
		if (success)
		{
			return REDIRECT_HOME_URL;
		}
		return REDIRECT_LOGIN_URL;

	}

}
