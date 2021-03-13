package com.klezovich.securitydemo.authprovider.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
@Profile("text_auth_provider")
public class TextFileAuthenticationProvider implements AuthenticationProvider {

    private final static String AUTH_FILE_NAME = "user_credentials.txt";
    private final static Map<String, String> userToPasswordMap = new HashMap<>();

    @Autowired
    public TextFileAuthenticationProvider() {
        loadUsersFromFileToMap();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();

        var password = userToPasswordMap.get(username);
        if (password == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        return new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority("user")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void loadUsersFromFileToMap() {
        try {
            var resource = getClass().getClassLoader().getResource(AUTH_FILE_NAME);
            var file = new File(resource.toURI());
            var scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                if (line.isBlank()) {
                    continue;
                }

                var tokens = line.split(",");
                userToPasswordMap.put(tokens[0], tokens[1]);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
