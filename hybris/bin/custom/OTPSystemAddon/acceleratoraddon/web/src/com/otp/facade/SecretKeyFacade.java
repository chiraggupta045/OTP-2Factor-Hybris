/**
 *
 */
package com.otp.facade;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import org.springframework.ui.Model;


/**
 * @author chiragupta
 *
 */
public interface SecretKeyFacade
{

	String checkUserAuthentication(Model model) throws Exception;

	/**
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	boolean validateCodeTypedByUser(String otp) throws Exception;

}
