package com.example.MyBookShopApp.services.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtils {
    public final String TOKEN_PATH = "/";

    public Cookie createTokenCookie(String value, int age) {
        Cookie cookie = new Cookie("token", value);
        cookie.setMaxAge(age);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath(TOKEN_PATH);
        return cookie;
    }
    public Cookie getCookieByName(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies){
            if(cookie.getName().equals(name)){
                return cookie;
            }
        }
        return null;
    }


}
