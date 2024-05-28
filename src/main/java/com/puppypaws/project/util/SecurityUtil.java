package com.puppypaws.project.util;

import com.puppypaws.project.model.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomOAuth2User) {
                CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;
                return customOAuth2User.getId();
            } else if (principal instanceof String) {
                return Long.valueOf((String) principal);
            }
        }
        return null;
    }
}
