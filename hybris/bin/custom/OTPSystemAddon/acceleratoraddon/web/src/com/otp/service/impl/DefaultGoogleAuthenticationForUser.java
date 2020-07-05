/**
 *
 */
package com.otp.service.impl;

import com.otp.service.GoogleAuthenticationForUser;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;


/**
 * @author mani
 *
 */
public class DefaultGoogleAuthenticationForUser implements GoogleAuthenticationForUser
{

	@Override
	public void createGoogleAuthenticationKey()
	{
		final GoogleAuthenticator gAuth = new GoogleAuthenticator();
		final GoogleAuthenticatorKey googleAuthkey = gAuth.createCredentials();
		final String key = googleAuthkey.getKey();


	}

	/*
	 * @Override public Map<String, String> qrCodeGeneration(final UserModel user) throws URISyntaxException, IOException
	 * { // XXX Auto-generated method stub return null; }
	 */

}
