package com.klezovich.securitydemo.rejectall;

import com.klezovich.securitydemo.common.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.klezovich.securitydemo.common.HelloController.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HelloController.class)
@ActiveProfiles("reject_all")
class RejectAllFilterTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testPublicApiAvailableWithoutCredentials() throws Exception {
        mvc.perform(get(PUBLIC_PATH))
                .andExpect(status().is(405))
                .andExpect(content().string(containsString("lockdown")));
    }

    @WithMockUser("spring")
    @Test
    void testPrivateApiAvailableWithoutCredentials() throws Exception {
        mvc.perform(get(PRIVATE_PATH))
                .andExpect(status().is(405))
                .andExpect(content().string(containsString("lockdown")));
    }
}