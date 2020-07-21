/**
 *
 */
package com.otp.controller;

import com.otp.data.OtpForm;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.otp.ControllerConstants;
import com.otp.facade.SecretKeyFacade;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
		boolean isUserEnabledFor2FactorAuth = false;
		LOG.info("Otp Controller to verify whether user is 2-factor enabled or First time logged in user");
		try
		{
			isUserEnabledFor2FactorAuth = secretKeyFacade.checkUserAuthentication();
			LOG.info(isUserEnabledFor2FactorAuth);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		OtpForm otpFormData = new OtpForm();
		model.addAttribute("otpFormData", otpFormData);
		model.addAttribute("isUserEnabledFor2FactorAuth",isUserEnabledFor2FactorAuth);
		model.addAttribute("userName",secretKeyFacade.getCustomerUserName());
		return ControllerConstants.Actions.Pages.Account.OTP;
	}


	/**
	 *
	 * @param otpFormData
	 * @return
	 */
	@RequestMapping(value = "/checkOTP", method = RequestMethod.POST)
	@RequireHardLogIn
	public String checkOTP(final OtpForm otpFormData, final BindingResult bindingResult,
		final Model model, final RedirectAttributes redirectAttributes)
	{
		LOG.info("OTP Controller to check the OTP authenticity by comparing it with Google Authenticator generated OTP");

		final boolean success = secretKeyFacade.validateCodeTypedByUser(otpFormData.getOtp());
		if (success)
		{
			return REDIRECT_HOME_URL;
		}
		return REDIRECT_LOGIN_URL;
	}
}