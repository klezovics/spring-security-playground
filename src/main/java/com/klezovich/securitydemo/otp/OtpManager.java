package com.klezovich.securitydemo.otp;

import java.util.Map;
import java.util.Objects;

public class OtpManager {

    private final Map<String, OneTimePassword> passwords;

    public OtpManager(Map<String, OneTimePassword> passwords) {
        this.passwords = passwords;
    }


    public boolean contains(String otp) {
        return passwords.containsKey(otp);
    }

    public boolean wasActivated(String otp) {
        var password = passwords.get(otp);
        if (password.getExpiryTimeEpochSeconds() == null) {
            return false;
        }

        return true;
    }

    public void activate(String otp) {
        var password = passwords.get(otp);
        if (Objects.isNull(password)) {
            throw new OtpNotFoundException();
        }

        password.setExpiryTimeEpochSeconds(epochSeconds() + OneTimePassword.DEFAULT_DURATION_SECONDS);
    }

    public boolean isExpired(String otp) {
        var password = passwords.get(otp);
        if (Objects.isNull(password)) {
            throw new OtpNotFoundException();
        }

        if (password.getExpiryTimeEpochSeconds() < epochSeconds()) {
            return true;
        }

        return true;
    }

    private long epochSeconds() {
        return System.currentTimeMillis() / 1000L;
    }

    public static class OtpNotFoundException extends RuntimeException {
        public OtpNotFoundException() {
        }

        public OtpNotFoundException(String msg) {
            super(msg);
        }

    }
}
