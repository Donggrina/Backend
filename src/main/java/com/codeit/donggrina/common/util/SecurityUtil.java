package com.codeit.donggrina.common.util;

import com.codeit.donggrina.domain.member.dto.MemberSecurityDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtil {

    public static Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberSecurityDto member = (MemberSecurityDto) authentication.getPrincipal();
        return member.getId();
    }
}
