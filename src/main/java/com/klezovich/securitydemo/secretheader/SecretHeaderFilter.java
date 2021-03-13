package com.klezovich.securitydemo.secretheader;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecretHeaderFilter extends OncePerRequestFilter {

    public static final String SECRET_HEADER_NAME = "SecretHeader";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (secretHeaderPresent(request)) {
            filterChain.doFilter(request, response);
        }else {
            response.setStatus(405);
            response.getWriter().println("Secret not present");
        }
    }

    private boolean secretHeaderPresent(HttpServletRequest request) {
        var headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                if (SECRET_HEADER_NAME.equals(headerNames.nextElement())) {
                    return true;
                }
            }
        }

        return false;
    }
}
