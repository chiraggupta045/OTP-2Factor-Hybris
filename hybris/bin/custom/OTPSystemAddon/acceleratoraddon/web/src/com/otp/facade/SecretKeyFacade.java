/**
 *
 */
package com.otp.facade;

/**
 * @author chiragupta
 *
 */
public interface SecretKeyFacade
{
	/**
	 * this method is to check loggedIn user is authenticate or not
	 * @return
	 */
	boolean checkUserAuthentication();

	/**this method is valide otp entered by user is valid or not
	 * @param otp
	 * @return
	 */
	boolean validateGoogleAuthBasedOtp(String otp);

	/**
	 *this method will return the username of customer
	 * @return username
	 */
	String getCustomerUserName();
}
