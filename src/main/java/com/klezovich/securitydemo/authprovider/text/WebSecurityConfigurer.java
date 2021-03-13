package com.klezovich.securitydemo.authprovider.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.klezovich.securitydemo.common.UserConfigurer.configureSingleUser;

@Configuration
@Profile("text_auth_provider")
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final TextFileAuthenticationProvider provider;

    @Autowired
    public WebSecurityConfigurer(TextFileAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        configureSingleUser(auth);
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/private/**")
                .authenticated()
                .antMatchers("/public/**")
                .permitAll()
                .and()
                .httpBasic();

    }
}