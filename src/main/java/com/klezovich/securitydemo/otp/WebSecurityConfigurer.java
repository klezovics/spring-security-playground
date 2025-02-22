package com.klezovich.securitydemo.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.klezovich.securitydemo.common.UserConfigurer.configureSingleUser;

@Configuration
@Profile("otp")
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final OtpAuthenticationFilter otpAuthenticationFilter;

    @Autowired
    public WebSecurityConfigurer(OtpAuthenticationFilter otpAuthenticationFilter) {
        this.otpAuthenticationFilter = otpAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        configureSingleUser(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(otpAuthenticationFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/private/**")
                .authenticated()
                .antMatchers("/public/**")
                .permitAll()
                .and()
                .httpBasic();

    }
}