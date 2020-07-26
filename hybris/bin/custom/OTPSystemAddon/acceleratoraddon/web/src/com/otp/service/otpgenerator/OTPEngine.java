package com.otp.service.otpgenerator;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.LoggerFactory;
import org.w3c.dom.css.Counter;

import javax.crypto.SecretKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;


public class OTPEngine extends HMACEngine {

    private RandomNumberGenerator rngInstance;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    static final int TIME_LAP_INTERVAL = 5; // in minutes

    static final int PASSWORD_EXPRIRY = 15; // in minutes

    static final int MAX_ATTEMPTS = 5; // number of attempts allowed to
    // validate an OTP

   // private static Logger logger = (Logger) LoggerFactory.getLogger(OTPEngine.class);

    TimeLaps timeLapGenerator;

    private Counter counterProvider;

    /**
     * Parameterized constructor. Creates new instance of OTP Engine.
     * Re-initializes Random Number generator.
     *
     * @param key
     * @param algorithm
     */
    private OTPEngine(SecretKey key, String algorithm, Counter counterProvider) {
        super(key, algorithm);
        if (rngInstance == null) {
            rngInstance = RandomNumberGenerator.getInstance();
            timeLapGenerator = new TimeLaps(TIME_LAP_INTERVAL);
            this.counterProvider = counterProvider;
        }
    }

  /*  *//**
     * Returns new instance of OTP Engine. The instance is not thread-safe. Do
     * not use this method in production. Use one that provides concrete
     * implementation of Counter Provider
     *
     * @param key
     *            - Secret key for generating HMAC
     * @return
     *//*
    public static OTPEngine getInstance(SecretKey key) {
        logger.warn("Using In-Memory Counter!! Not recommended for production deployments");
        return getInstance(key, "HmacSHA1", new InMemoryCounter());
    }*/

    /**
     * Returns new instance of OTP Engine. The instance is not thread-safe.
     *
     * @param key
     * @param counterProvider
     * @return
     */
    public static OTPEngine getInstance(SecretKey key, Counter counterProvider) {
        return getInstance(key, "HmacSHA1", counterProvider);
    }

    /**
     * Returns new instance of OTP Engine. The instance is not thread-safe.
     *
     * @param key
     * @param algorithm
     * @param counterProvider
     * @return
     */
    public static OTPEngine getInstance(SecretKey key, String algorithm, Counter counterProvider) {
        return new OTPEngine(key, algorithm, counterProvider);
    }

    /**
     * Generates a one-time password based on the given parameter strings.
     *
     * @param params
     * @return
     */
    public OTP generatePasswordWithHmac(final String params[]) {

        // generate a random password
        String password = Integer.toString(rngInstance.getRandomInt());

        // Include start of last time lap as one of the parameters in generating
        // HMAC
        TimeLaps timeLaps = new TimeLaps(TIME_LAP_INTERVAL);
        Calendar lastTimeLap = timeLaps.getPreviousTimeLap();
        String dateParam = dateFormat.format(lastTimeLap.getTime());

        String[] paramsForHMAC = (String[]) ArrayUtils.add(params, dateParam);

        String hmac = new StringBuffer(generateHMAC(password, paramsForHMAC)).append("O")
                .append(generateHMAC(password, params)).toString();

        return new OTP(password, hmac).generateChecksum();
    }}