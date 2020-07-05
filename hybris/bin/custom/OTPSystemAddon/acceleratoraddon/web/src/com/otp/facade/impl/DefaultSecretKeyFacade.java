/**
 *
 */
package com.otp.facade.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

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

	@Override
	public String checkUserAuthentication(final Model model) throws Exception
	{

		if (userService.getCurrentUser().isIsEnabledTwoFactorAuthentication())
		{
			// dont do anything
			return "Two factor Authentication is already enabled for this user";
		}
		else if (!userService.getCurrentUser().isIsEnabledTwoFactorAuthentication()
				&& StringUtils.isEmpty(userService.getCurrentUser().getSecretKeyForOTP()))
		{
			final UserModel userModel = userService.getCurrentUser();
			final String secretKey = generateSecretKey();
			userModel.setIsEnabledTwoFactorAuthentication(Boolean.TRUE);
			userModel.setSecretKeyForOTP(secretKey);
			getGoogleAuthenticatorBarCode(secretKey, model);
			modelService.save(userModel);
			return "New User Enabled for 2 factor Authentication:-" + userModel.getUid();
		}
		return "";
	}

	/**
	 * Generate A random Secret Key from the GoogleAuthenticator Api's
	 *
	 * @return
	 * @throws Exception
	 */
	public String generateSecretKey() throws Exception
	{
		final GoogleAuthenticator gAuth = new GoogleAuthenticator();
		final GoogleAuthenticatorKey googleAuthKey = gAuth.createCredentials();
		return googleAuthKey.getKey();
	}

	/**
	 *
	 * @param secretKey
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public void getGoogleAuthenticatorBarCode(final String secretKey, final Model model) throws Exception
	{
		// Company name
		final String companyName = "HPE PointNext";
		// Fetching the current user logged in
		final String userEmail = userService.getCurrentUser().getUid();
		// Generating the bar code data for the current user and secret key tagged to the user.
		final String barCodeData = "otpauth://totp/" + URLEncoder.encode(companyName + ":" + userEmail, "UTF-8").replace("+", "%20")
				+ "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20") + "&issuer="
				+ URLEncoder.encode(companyName, "UTF-8").replace("+", "%20");
		// Create QR Code based on the bar code data generated for the current logged in user and secret key associated to the user.
		createQRCode(barCodeData, model);
	}

	/**
	 *
	 * @param barCodeData
	 * @throws Exception
	 */
	public void createQRCode(final String barCodeData, final Model model) throws Exception
	{
		final UserModel userModel = userService.getCurrentUser();
		final String userName = userModel.getName();
		final ByteMatrix result = new QRCodeWriter().encode(barCodeData, BarcodeFormat.QR_CODE, 400, 400);
		final BitMatrix bitMatrix = convertByteMatrixToBitMatrix(result);
		final FileOutputStream out = new FileOutputStream(
				configurationService.getConfiguration().getString("otp.qr.code.images") + userName + ".png");
		MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
	}

	/**
	 *
	 * @param matrix
	 * @return
	 */
	private BitMatrix convertByteMatrixToBitMatrix(final ByteMatrix matrix)
	{
		final int matrixWidgth = matrix.getWidth();
		final int matrixHeight = matrix.getHeight();
		final BitMatrix output = new BitMatrix(matrixWidgth, matrixHeight);
		output.clear();
		for (int i = 0; i < matrixWidgth; i++)
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


	@Override
	public boolean validateCodeTypedByUser(final String otp) throws Exception
	{
		final UserModel userModel = userService.getCurrentUser();

		if (StringUtils.isNotEmpty(userModel.getSecretKeyForOTP()))
		{
			final GoogleAuthenticator gAuth = new GoogleAuthenticator();

			final boolean gAuthStatus = gAuth.authorize(userModel.getSecretKeyForOTP(), Integer.valueOf(otp));
			return gAuthStatus;
		}
		return false;
	}
}