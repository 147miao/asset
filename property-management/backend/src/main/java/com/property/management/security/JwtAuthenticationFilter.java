package com.property.management.security;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(header);
        if (StrUtil.isBlank(authHeader) || !authHeader.startsWith(prefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(prefix.length()).trim();
        if (jwtTokenUtil.isTokenExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = jwtTokenUtil.getUserId(token);
        String username = jwtTokenUtil.getUsername(token);
        String userType = jwtTokenUtil.getUserType(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType.toUpperCase()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
