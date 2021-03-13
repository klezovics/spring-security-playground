package com.klezovich.securitydemo.secretheader;

import com.klezovich.securitydemo.common.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.klezovich.securitydemo.common.HelloController.PUBLIC_HELLO;
import static com.klezovich.securitydemo.common.HelloController.PUBLIC_PATH;
import static com.klezovich.securitydemo.secretheader.SecretHeaderFilter.SECRET_HEADER_NAME;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HelloController.class)
@ActiveProfiles("secret_header")
class SecretHeaderFilterTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testWithoutSecretHeaderPublicApiUnavailable() throws Exception {
        mvc.perform(get(PUBLIC_PATH))
                .andExpect(status().is(405))
                .andExpect(content().string(containsString("Secret not present")));
    }

    @Test
    void testWithSecreetHeaderPublicApiAvailable() throws Exception {
        mvc.perform(get(PUBLIC_PATH).header(SECRET_HEADER_NAME,"secret"))
                .andExpect(status().is(200))
                .andExpect(content().string(PUBLIC_HELLO));
    }
}