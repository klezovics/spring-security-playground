package com.klezovich.securitydemo.basicauth;

import com.klezovich.securitydemo.common.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.klezovich.securitydemo.common.HelloController.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@ActiveProfiles("basic_auth")
class BasicAuthTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testPublicApiAvailableWithoutCredentials() throws Exception {
        mvc.perform(get(PUBLIC_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string(PUBLIC_HELLO));
    }

    @Test
    void testPrivateApiNotAvailableWithoutCredentials() throws Exception {
        mvc.perform(get(PRIVATE_PATH))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(value = "spring")
    @Test
    void testPrivateApiAvailableWithCredentials() throws Exception {
        mvc.perform(get(PRIVATE_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string(PRIVATE_HELLO));
    }
}