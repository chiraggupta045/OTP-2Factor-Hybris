package com.otp;


public interface ControllerConstants
{
	// Constant names cannot be changed due to their usage in dependant extensions, thus nosonar

	/**
	 * Class with action name constants
	 */
	interface Actions
	{
		interface Cms // NOSONAR
		{
			String ADDON_PREFIX = "addon:/OTPSystemAddon/";

		}

		interface Pages
		{
			interface Account // NOSONAR
			{
				String OTP = Cms.ADDON_PREFIX + "pages/otpTwoFactorLogin";
			}

		}
	}
}