package com.klezovich.securitydemo.otp;

public class OneTimePassword {

    public static final int DEFAULT_DURATION_SECONDS = 1000;

    private String value;
    private Long expiryTimeEpochSeconds;

    public OneTimePassword(String value, Long expiryTimeEpochSeconds) {
        this.value = value;
        this.expiryTimeEpochSeconds = expiryTimeEpochSeconds;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpiryTimeEpochSeconds() {
        return expiryTimeEpochSeconds;
    }

    public void setExpiryTimeEpochSeconds(Long expiryTimeEpochSeconds) {
        this.expiryTimeEpochSeconds = expiryTimeEpochSeconds;
    }
}
