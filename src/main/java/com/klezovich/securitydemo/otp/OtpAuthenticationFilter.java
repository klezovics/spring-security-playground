package com.klezovich.securitydemo.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@Profile("otp")
public class OtpAuthenticationFilter extends OncePerRequestFilter {

    public static final String OTP_HEADER_NAME = "Otp";
    public final OtpManager otpManager;


    @Autowired
    public OtpAuthenticationFilter(OtpManager otpManager) {
        this.otpManager = otpManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var otp = request.getHeader(OTP_HEADER_NAME);
        if (Objects.isNull(otp)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!otpManager.contains(otp)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!otpManager.wasActivated(otp)) {
            otpManager.activate(otp);
            authenticateByOtp(otp);
            return;
        }

        if (otpManager.isExpired(otp)) {
            response.setStatus(401);
            response.getWriter().println("OTP expired");
            return;
        }

        filterChain.doFilter(request, response);
    }


    private void authenticateByOtp(String otp) {
        var user = new UsernamePasswordAuthenticationToken("otp_user",
                otp,
                List.of(new SimpleGrantedAuthority("otp_user")));

        SecurityContextHolder.getContext().setAuthentication(user);
    }
}
