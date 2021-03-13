package com.klezovich.securitydemo.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    public static final String PUBLIC_PATH="/public/hello";
    public static final String PRIVATE_PATH="/private/hello";

    public static final String PUBLIC_HELLO="Public Hello";
    public static final String PRIVATE_HELLO="Private Hello";

    @GetMapping(PUBLIC_PATH)
    public String helloPublic() {
        return PUBLIC_HELLO;
    }

    @GetMapping(PRIVATE_PATH)
    public String helloPrivate() {
        return PRIVATE_HELLO;
    }

}
