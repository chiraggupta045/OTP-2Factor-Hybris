package com.otp.data;

import java.io.Serializable;

public class OtpForm implements Serializable
{

    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
