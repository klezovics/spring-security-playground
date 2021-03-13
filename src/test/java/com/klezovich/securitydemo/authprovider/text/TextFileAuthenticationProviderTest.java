package com.klezovich.securitydemo.authprovider.text;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.klezovich.securitydemo.common.HelloController.PRIVATE_PATH;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ActiveProfiles("text_auth_provider")
class TextFileAuthenticationProviderTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testUserNotPresentForAnyProviderCantLogin() throws Exception {
        mvc.perform(get(PRIVATE_PATH))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUsersFromTextFileAuthenticationProviderCanLogIn() throws Exception {
        mvc.perform(get(PRIVATE_PATH).with(httpBasic("test_user1", "ptu1")))
                .andExpect(status().isOk());

        mvc.perform(get(PRIVATE_PATH).with(httpBasic("test_user2", "ptu2")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("spring")
    void testUsersFromDaoAuthenticationProviderCanAlsoLogIn() throws Exception {
        mvc.perform(get(PRIVATE_PATH))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public TextFileAuthenticationProvider getSomeBean() {
            return new TextFileAuthenticationProvider();
        }
    }
}