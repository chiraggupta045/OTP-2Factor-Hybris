/**
 *
 */
package com.otp.controller;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.otp.ControllerConstants;
import com.otp.facade.OtpFacade;
import com.otp.facade.SecretKeyFacade;


/**
 * @author chiragupta
 *
 */

@Controller
@RequestMapping(value = "/otp")
public class OTPController extends AbstractPageController
{
	@Autowired
	private OtpFacade otpFacade;

	@Autowired
	private SecretKeyFacade secretKeyFacade;

	@Autowired
	private ConfigurationService configurationService;

	private static final String REDIRECT_HOME_URL = REDIRECT_PREFIX + "/";
	private static final String REDIRECT_LOGIN_URL = REDIRECT_PREFIX + "/login";


	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String getOTP(final Model model)
	{
		System.out.println("==========================OTP Custom Controller=========");

		try
		{
			final String userAuthStatus = secretKeyFacade.checkUserAuthentication(model);
			System.out.println(userAuthStatus);
		}
		catch (final Exception e)
		{
			System.out.println(e);
		}
		model.addAttribute("qrCodePath", configurationService.getConfiguration().getString("otp.qr.code.images"));
		return ControllerConstants.Actions.Pages.Account.Otp;
	}

	@RequestMapping(value = "/checkOTP", method = RequestMethod.GET)
	public String checkOTP(@RequestParam(name = "otp")
	final String otp) throws Exception
	{
		System.out.println("========================== Inside Check otp");

		final boolean success = secretKeyFacade.validateCodeTypedByUser(otp);
		if (success)
		{
			return REDIRECT_HOME_URL;
		}
		return REDIRECT_LOGIN_URL;

	}

}
