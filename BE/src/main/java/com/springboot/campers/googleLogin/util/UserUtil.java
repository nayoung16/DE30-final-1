package com.springboot.campers.googleLogin.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtil {
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //인증 객체에 Principal로 얻은 정보 저장
        if(authentication == null || !authentication.isAuthenticated()) //인증값이 false이거나 null인지 확인
        {
            return null;
        }

        Object principal = authentication.getPrincipal(); //authentication 이 비어있지 않은걸 확인했으니 그 값을 저장
        if (principal instanceof UserDetails) {
            System.out.println( ((UserDetails) principal).getUsername());
            return ((UserDetails) principal).getUsername();

        } else {
            System.out.println(principal.toString());
            return principal.toString();
        }
    }
}
