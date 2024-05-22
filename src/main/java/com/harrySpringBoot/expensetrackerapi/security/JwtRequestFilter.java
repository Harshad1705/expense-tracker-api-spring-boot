package com.harrySpringBoot.expensetrackerapi.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.harrySpringBoot.expensetrackerapi.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtRequestFilter extends OncePerRequestFilter {
    

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
    
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String userName = null;
       
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            try {
                userName = jwtTokenUtil.getUserNameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Unable to get JWT token");

            } catch (ExpiredJwtException e) {
                throw new RuntimeException("Jwt token has expired");
            }
        }
        
        // once we get the token, validate the token
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(userName);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
