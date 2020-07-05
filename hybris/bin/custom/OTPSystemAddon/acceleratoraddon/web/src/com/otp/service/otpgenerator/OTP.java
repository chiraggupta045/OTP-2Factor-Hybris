package com.otp.service.otpgenerator;

public class OTP {

    String password;
    String hmac;
    int instance = 0;

    public OTP(String pass, String hmac) {
        this.password = pass;
        this.hmac = hmac;
    }

    public OTP(String pass, String hmac, int instance) {
        this.password = pass;
        this.hmac = hmac;
        this.instance = instance;
    }

    public String getPassword() {
        return password;
    }

    public String getHmac() {
        return hmac;
    }

    public OTP generateChecksum() {
        int checksum = numberChecksum(Integer.parseInt(password));
        return new OTP(password + checksum, hmac);
    }

    public OTP stripChecksum() {
        return new OTP(String.valueOf(Integer.parseInt(password) / 10), hmac);
    }

    public boolean hasValidChecksum() {
        int generateChecksum = numberChecksum(Integer.parseInt(password) / 10);
        int lastDigit = Integer.parseInt(password) % 10;
        return (generateChecksum == lastDigit);
    }

    private int numberChecksum(final int original) {
        int checksum = 0, counter = 1;
        int numberReduced = original;
        while (numberReduced > 0) {
            if (counter % 2 == 0) {
                checksum += numberReduced % 10;
            } else {
                checksum += (numberReduced % 10) * 3;
            }
            numberReduced /= 10;
        }
        if (checksum > 0 && checksum > 9) {
            checksum = (10 - (checksum % 10)) % 10;
        }
        return checksum;
    }

}