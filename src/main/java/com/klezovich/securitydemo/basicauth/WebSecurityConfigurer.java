package com.klezovich.securitydemo.basicauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.klezovich.securitydemo.common.UserConfigurer.configureSingleUser;

@Configuration
@Profile("basic_auth")
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        configureSingleUser(auth);
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