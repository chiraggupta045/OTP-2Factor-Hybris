package com.otp.handler;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.otp.facade.OtpFacade;
import com.otp.service.otpgenerator.OTP;


public class OTPGUIDAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{

	private GUIDCookieStrategy guidCookieStrategy;
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	private OtpFacade otpFacade;
	private SessionService sessionService;


	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{

		getGuidCookieStrategy().setCookie(request, response);
		try
		{
			//generate secret key
			final SecretKey key = getSecretkey();
			//generate otp
			final String uid = (String) authentication.getPrincipal();
			final OTP otp = otpFacade.generateOtpForAuthentication(uid, key);

			// send otp
			sessionService.setAttribute("otp", otp.getPassword());
			otpFacade.sendOtpForVerification("7876222279", otp.getPassword());


		}
		catch (final NoSuchAlgorithmException e)
		{

			e.printStackTrace();
		}

		getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
	}






	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}


	/**
	 * @param guidCookieStrategy
	 *           the guidCookieStrategy to set
	 */
	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected AuthenticationSuccessHandler getAuthenticationSuccessHandler()
	{
		return authenticationSuccessHandler;
	}

	/**
	 * @param authenticationSuccessHandler
	 *           the authenticationSuccessHandler to set
	 */
	@Required
	public void setAuthenticationSuccessHandler(final AuthenticationSuccessHandler authenticationSuccessHandler)
	{
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	private SecretKey getSecretkey() throws NoSuchAlgorithmException
	{
		KeyGenerator kgen;
		kgen = KeyGenerator.getInstance("AES");
		final SecretKey skey = kgen.generateKey();
		return skey;
	}

}
