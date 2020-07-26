/**
 *
 */
package com.otp.controller;

import com.otp.data.OtpForm;
import com.otp.enums.AuthenticationType;
import com.otp.facade.OtpFacade;
import com.otp.service.otpgenerator.OTP;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.otp.ControllerConstants;
import com.otp.facade.SecretKeyFacade;
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
	private OtpFacade otpFacade;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private BaseStoreService baseStoreService;

	@Autowired
	private SessionService sessionService;

	private static final String REDIRECT_HOME_URL = REDIRECT_PREFIX + "/";
	private static final String REDIRECT_LOGIN_URL = REDIRECT_PREFIX + "/login";

	/**
	 *This method is to verify and get the otp
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String getOTP(final Model model)
	{
		boolean isUserAlreadyEnabledFor2FactorAuth = false;
		LOG.debug("OTP Controller to verify whether user is enabled for Google Authentication or SMS based OTP");

		BaseStoreModel baseStoreModel = baseStoreService.getCurrentBaseStore();
		AuthenticationType authenticationType =baseStoreModel.getTypeOfAuthentication();
		try
		{
			if(AuthenticationType.GOOGLE_AUTHENTICATION.equals(authenticationType))
			{
				isUserAlreadyEnabledFor2FactorAuth = secretKeyFacade.checkUserAuthentication();
				LOG.info("Logged in user is enabled for Google Authentication");
			}
			else if(AuthenticationType.OTP.equals(authenticationType))
			{
				OTP otp =otpFacade.generateOtpForAuthentication();
				otpFacade.sendOtpForVerification(otp.getPassword());
				sessionService.setAttribute("OTP",otp.getPassword());
				LOG.info("Logged in user is enabled for SMS Based OTP ");
				model.addAttribute("smsBasedOTP", Boolean.TRUE);
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		OtpForm otpFormData = new OtpForm();
		model.addAttribute("otpFormData", otpFormData);
		model.addAttribute("isUserAlreadyEnabledFor2FactorAuth",isUserAlreadyEnabledFor2FactorAuth);
		model.addAttribute("userName",secretKeyFacade.getCustomerUserName());
		return ControllerConstants.Actions.Pages.Account.OTP;
	}


	/**
	 * this method is to check whether the entered otp by user is valid or not
	 * @param otpFormData
	 * @return
	 */
	@RequestMapping(value = "/checkOTP", method = RequestMethod.POST)
	@RequireHardLogIn
	public String checkOTP(final OtpForm otpFormData, final BindingResult bindingResult,
		final Model model, final RedirectAttributes redirectAttributes)
	{
		LOG.info("OTP Controller to check the OTP validity Entered by the user");
		boolean success = false;
		if (bindingResult.hasErrors())
		{
			LOG.error("OTP Form Data has binding error's, so returning back to login page");
			return REDIRECT_LOGIN_URL;
		}
		success = verifyOTP(otpFormData);
		if (success)
		{
			return REDIRECT_HOME_URL;
		}
		return REDIRECT_LOGIN_URL;
	}

	/**
	 *
	 * @param otpFormData
	 * @return
	 */
	private boolean verifyOTP(OtpForm otpFormData)
	{
		boolean success;
		if(null != sessionService.getAttribute("OTP"))
		{
			LOG.info("Validating the OTP received through SMS in mobile");
			success = otpFacade.validateSMSBasedOtp(otpFormData.getOtp());
		}
		else {
			LOG.info("Verifying the OTP received through Google Authenticator App in mobile");
			success = secretKeyFacade.validateGoogleAuthBasedOtp(otpFormData.getOtp());
		}
		return success;
	}
}