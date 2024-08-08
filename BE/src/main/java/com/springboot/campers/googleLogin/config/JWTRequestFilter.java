package com.springboot.campers.googleLogin.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTRequestFilter.class);
    private final JWTUtils jwtUtils;

    public JWTRequestFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("doFilterInternal 시작");
        System.out.println("현재getAuthentication안에 들어있는것 :" + SecurityContextHolder.getContext().getAuthentication());
//        System.out.println(request.getHeader("Authorization"));
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = null;
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
//            token = authorizationHeader.substring(7);
//        }
//        System.out.println(token);

        Cookie[] cookies = request.getCookies();
        String cookies2 = response.getHeader("AUTH-TOKEN");
        System.out.println(cookies2);
        System.out.println("request 객체: " + request);
        System.out.println("HttpServletRequest로부터 불러온 쿠키입니다: " + Arrays.toString(cookies));
        if (cookies != null) {
            System.out.println("Cookies found: " + Arrays.toString(cookies));
            for (Cookie cookie: cookies){
                System.out.println("Cookie Name: " + cookie.getName());
                System.out.println("Cookie Value: " + cookie.getValue());
            }
            Cookie authCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("AUTH-TOKEN"))
                    .findAny().orElse(null);
            ;

            if (authCookie != null) {
                System.out.println("Auth Cookie found: " + authCookie);
                System.out.println("AuthCookies.getValue: "+authCookie.getValue());
                Authentication authentication = jwtUtils.verifyAndGetAuthentication(authCookie.getValue());
                System.out.println("JWTRequest에서 생성된 authentication 입니다: " + authentication);
                if (authentication != null) {
                    System.out.println("Authentication successful: " + authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("쿠키로부터 지정된 authentication 입니다: " + authentication);
                } else {
                    System.out.println("Authentication failed");
                }
            } else {
                System.out.println("Auth Cookie not found");
            }
        } else {
            System.out.println("No cookies found");
        }
        System.out.println("Exiting JWTRequestFilter");
        filterChain.doFilter(request, response);
    }
}

