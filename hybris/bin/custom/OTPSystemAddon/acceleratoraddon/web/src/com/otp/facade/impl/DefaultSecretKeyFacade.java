/**
 *
 */
package com.otp.facade.impl;

import com.otp.constants.OTPSystemAddonFacadeConstants;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.FileOutputStream;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.otp.facade.SecretKeyFacade;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;


/**
 * @author chiragupta
 *
 */
@Component
public class DefaultSecretKeyFacade implements SecretKeyFacade
{

	@Autowired
	private UserService userService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(DefaultSecretKeyFacade.class);

	@Override
	public boolean checkUserAuthentication() throws Exception
	{
		if (userService.getCurrentUser().isIsEnabledTwoFactorAuthentication())
		{
			// Do Nothing and return TRUE
			LOG.info("Two factor Authentication is already enabled for this user");
			return Boolean.TRUE;
		}
		else if (!userService.getCurrentUser().isIsEnabledTwoFactorAuthentication()
				&& StringUtils.isEmpty(userService.getCurrentUser().getSecretKeyForOTP()))
		{
			final UserModel userModel = userService.getCurrentUser();
			final String secretKey = generateSecretKey();
			if(StringUtils.isNotEmpty(secretKey))
			{
				userModel.setIsEnabledTwoFactorAuthentication(Boolean.TRUE);
				userModel.setSecretKeyForOTP(secretKey);
				getGoogleAuthenticatorBarCode(secretKey);
				modelService.save(userModel);
				LOG.info("New User Enabled for 2-Factor Authentication:" + userModel.getUid());
				return Boolean.FALSE;
			}
			else {
				LOG.error("Secret Key was not generated for the Logged in User");
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * Generate A random Secret Key from the GoogleAuthenticator API
	 *
	 * @return
	 * @throws Exception
	 */
	public String generateSecretKey() throws Exception
	{
		final GoogleAuthenticator gAuth = new GoogleAuthenticator();
		try {
			final GoogleAuthenticatorKey googleAuthKey = gAuth.createCredentials();
			return googleAuthKey.getKey();
		}
		catch (Exception e)
		{
			LOG.error("Error occurred while creating secret key using GoogleAuthenticator API" +e.getMessage());
		}
		return "";
	}

	/**
	 *
	 * @param secretKey
	 * @throws Exception
	 */

	public void getGoogleAuthenticatorBarCode(final String secretKey) throws Exception
	{
		// Company name
		final String companyName = OTPSystemAddonFacadeConstants.COMPANY_NAME;
		// Fetching the current user logged in
		final String userEmail = userService.getCurrentUser().getUid();
		// Generating the bar code data for the current user and secret key tagged to the user.
		final String barCodeData = OTPSystemAddonFacadeConstants.QR_CODE_DATA_HEADER + URLEncoder.encode(companyName + OTPSystemAddonFacadeConstants.COLON + userEmail,
				OTPSystemAddonFacadeConstants.QR_CODE_ENCODE_TYPE).replace(OTPSystemAddonFacadeConstants.PLUS, OTPSystemAddonFacadeConstants.QR_C0DE_CHAR_SEQ)
				+ OTPSystemAddonFacadeConstants.QR_CODE_SECRET + URLEncoder.encode(secretKey, OTPSystemAddonFacadeConstants.QR_CODE_ENCODE_TYPE)
				.replace(OTPSystemAddonFacadeConstants.PLUS, OTPSystemAddonFacadeConstants.QR_C0DE_CHAR_SEQ) + OTPSystemAddonFacadeConstants.QR_CODE_ISSUER
				+ URLEncoder.encode(companyName, OTPSystemAddonFacadeConstants.QR_CODE_ENCODE_TYPE).replace(OTPSystemAddonFacadeConstants.PLUS, OTPSystemAddonFacadeConstants.QR_C0DE_CHAR_SEQ);
		// Create QR Code based on the bar code data generated for the current logged in user and secret key associated to the user.
		createQRCode(barCodeData);
	}

	/**
	 *
	 * @param barCodeData
	 * @throws Exception
	 */
	public void createQRCode(final String barCodeData) throws Exception
	{
		final UserModel userModel = userService.getCurrentUser();
		final String userName = userModel.getName();
		final ByteMatrix result = new QRCodeWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
				OTPSystemAddonFacadeConstants.WIDTH, OTPSystemAddonFacadeConstants.HEIGHT);
		final BitMatrix bitMatrix = convertByteMatrixToBitMatrix(result);
		final FileOutputStream out = new FileOutputStream(
				configurationService.getConfiguration().getString(OTPSystemAddonFacadeConstants.OTP_QR_CODE) +
						userName + OTPSystemAddonFacadeConstants.DOT + OTPSystemAddonFacadeConstants.IMAGE_FORMAT);
		MatrixToImageWriter.writeToStream(bitMatrix, OTPSystemAddonFacadeConstants.IMAGE_FORMAT, out);
	}

	/**
	 *
	 * @param matrix
	 * @return
	 */
	private BitMatrix convertByteMatrixToBitMatrix(final ByteMatrix matrix)
	{
		final int matrixWidth = matrix.getWidth();
		final int matrixHeight = matrix.getHeight();
		final BitMatrix output = new BitMatrix(matrixWidth, matrixHeight);
		output.clear();
		for (int i = 0; i < matrixWidth; i++)
		{
			for (int j = 0; j < matrixHeight; j++)
			{
				if (matrix.get(i, j) == -1)
				{
					output.set(i, j);
				}
			}
		}
		return output;
	}

	/**
	 *
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean validateCodeTypedByUser(final String otp)
	{
		final UserModel userModel = userService.getCurrentUser();
		if (StringUtils.isNotEmpty(userModel.getSecretKeyForOTP()))
		{
			final GoogleAuthenticator gAuth = new GoogleAuthenticator();
			try {
				return gAuth.authorize(userModel.getSecretKeyForOTP(), Integer.parseInt(otp));
			}
			catch (Exception e)
			{
				LOG.error("Error occurred in comparison of OTP Entered and the OTP Generated by the GA App." +e.getMessage());
			}
		}
		return false;
	}

	@Override
	public String getCustomerUserName()
	{
		final UserModel userModel = userService.getCurrentUser();
		return userModel.getName();
	}
}