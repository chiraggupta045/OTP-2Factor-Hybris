package com.otp.constants;

public final class OTPSystemAddonFacadeConstants {

    private OTPSystemAddonFacadeConstants() {
    }
    // constansts for qr image for otp
    public static final Integer WIDTH = 200;
    public static final Integer HEIGHT = 200;

    public static final String DOT = ".";
    public static final String IMAGE_FORMAT = "png";
    public static final String COMPANY_NAME = "DELOITTE_USI";
    public static final String QR_CODE_DATA_HEADER = "otpauth://totp/";
    public static final String QR_CODE_ENCODE_TYPE = "UTF-8";
    public static final String COLON = ":";
    public static final String PLUS = "+";
    public static final String QR_C0DE_CHAR_SEQ = "%20";
    public static final String QR_CODE_SECRET = "?secret=";
    public static final String QR_CODE_ISSUER = "&issuer=";
    public static final String SLASH = "/";

    public static final String  OTP_QR_CODE  = "otp.qr.code.images";



}
