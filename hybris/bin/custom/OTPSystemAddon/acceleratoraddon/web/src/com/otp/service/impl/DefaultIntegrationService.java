/**
 *
 */
package com.otp.service.impl;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.otp.constants.OTPSystemAddonFacadeConstants;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.otp.OtpRequestPayload;
import com.otp.response.pojo.OtpResponse;
import com.otp.service.OtpIntegrationService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Component
public class DefaultIntegrationService implements OtpIntegrationService
{
	/** Configuration Service */
	@Autowired
	ConfigurationService configurationService;

	public static String OTP_URL = "com.hpe.otp.url";

	/** Logger Instance for DefaultIntegrationService */
	private static final Logger LOG = Logger.getLogger(DefaultIntegrationService.class);

	/**
	 * this method will generate Qr Code on the basis of secret key
	 * @param secretKey
	 * @param userService
	 */
	@Override
	public void getGoogleAuthenticatorBarCode(String secretKey, UserService userService)
	{
		// Company name
		final String companyName = OTPSystemAddonFacadeConstants.COMPANY_NAME;
		// Fetching the current user logged in
		final String userEmail = userService.getCurrentUser().getUid();
		try {
			// Generating the bar code data for the current user and secret key tagged to the user.
			final String barCodeData = OTPSystemAddonFacadeConstants.QR_CODE_DATA_HEADER + URLEncoder.encode(companyName + OTPSystemAddonFacadeConstants.COLON + userEmail,
					OTPSystemAddonFacadeConstants.QR_CODE_ENCODE_TYPE).replace(OTPSystemAddonFacadeConstants.PLUS, OTPSystemAddonFacadeConstants.QR_C0DE_CHAR_SEQ)
					+ OTPSystemAddonFacadeConstants.QR_CODE_SECRET + URLEncoder.encode(secretKey, OTPSystemAddonFacadeConstants.QR_CODE_ENCODE_TYPE)
					.replace(OTPSystemAddonFacadeConstants.PLUS, OTPSystemAddonFacadeConstants.QR_C0DE_CHAR_SEQ) + OTPSystemAddonFacadeConstants.QR_CODE_ISSUER
					+ URLEncoder.encode(companyName, OTPSystemAddonFacadeConstants.QR_CODE_ENCODE_TYPE).replace(OTPSystemAddonFacadeConstants.PLUS, OTPSystemAddonFacadeConstants.QR_C0DE_CHAR_SEQ);
			// Create QR Code based on the bar code data generated for the current logged in user and secret key associated to the user.
			createQRCode(barCodeData,userService);
		}
		catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage());
		}

	}

	/**
	 *this method will generate Qr Code that user will scan in google authenticator app
	 * @param barCodeData
	 * @param userService
	 * @throws Exception
	 */
	public void createQRCode(final String barCodeData, UserService userService)
	{
		final UserModel userModel = userService.getCurrentUser();
		final String userName = userModel.getName();
		try
		{
			final ByteMatrix result = new QRCodeWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
					OTPSystemAddonFacadeConstants.WIDTH, OTPSystemAddonFacadeConstants.HEIGHT);
			final BitMatrix bitMatrix = convertByteMatrixToBitMatrix(result);

			final FileOutputStream out = new FileOutputStream(
					configurationService.getConfiguration().getString(OTPSystemAddonFacadeConstants.OTP_QR_CODE) + OTPSystemAddonFacadeConstants.SLASH +
							userName + OTPSystemAddonFacadeConstants.DOT + OTPSystemAddonFacadeConstants.IMAGE_FORMAT);
			MatrixToImageWriter.writeToStream(bitMatrix, OTPSystemAddonFacadeConstants.IMAGE_FORMAT, out);
		}
		catch(IOException | WriterException e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * convert ByteMatrix to BitMatrix
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
	 * this method is to send the otp to registered mobile no
	 * @param requestPayLoad
	 * @param restTemplate
	 * @param headers
	 * @return
	 */
	@Override
	public ResponseEntity<OtpResponse> sendOtpForVerification(final OtpRequestPayload requestPayLoad,
			final RestTemplate restTemplate, final HttpHeaders headers)
	{
		LOG.debug("sending the otp to registered no");
		final HttpEntity<OtpRequestPayload> entityForPostCall = new HttpEntity<>(requestPayLoad, headers);
		return restTemplate.exchange(configurationService.getConfiguration().getString(OTP_URL), HttpMethod.POST, entityForPostCall, OtpResponse.class);
	}
}