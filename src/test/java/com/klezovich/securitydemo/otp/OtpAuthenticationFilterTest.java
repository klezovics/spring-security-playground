package com.klezovich.securitydemo.otp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static com.klezovich.securitydemo.common.HelloController.PRIVATE_PATH;
import static com.klezovich.securitydemo.otp.OtpAuthenticationFilter.OTP_HEADER_NAME;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ActiveProfiles("otp")
class OtpAuthenticationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testCantLoginWithoutValidOtp() throws Exception {
        mvc.perform(get(PRIVATE_PATH))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCanLoginWithOtp() throws Exception {
        mvc.perform(get(PRIVATE_PATH).header(OTP_HEADER_NAME, "otp_1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCantLoginWithExpiredOtp() throws Exception {
        mvc.perform(get(PRIVATE_PATH).header(OTP_HEADER_NAME, "otp_expired"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("OTP expired")));
    }

    @Test
    void testCanLoginWithBasicAuth() throws Exception {
        mvc.perform(get(PRIVATE_PATH).with(httpBasic("spring", "secret")))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public OtpManager otpManager() {
            var passwords = new HashMap<String,OneTimePassword>();
            passwords.put("otp_1", new OneTimePassword("otp_1", null));
            passwords.put("otp_expired", new OneTimePassword("otp_expired", 10L));
            return new OtpManager(passwords);
        }
    }
}