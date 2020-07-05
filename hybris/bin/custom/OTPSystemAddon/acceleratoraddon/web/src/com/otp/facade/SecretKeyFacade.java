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
	 *
	 * @return
	 * @throws Exception
	 */
	String checkUserAuthentication() throws Exception;

	/**
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	boolean validateCodeTypedByUser(String otp) throws Exception;

}
