package com.ProjectManagement.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ProjectManagement.entities.User;
import com.ProjectManagement.services.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String path = request.getServletPath();

        // Laisser passer le preflight CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Endpoints publics: pas besoin de JWT
        if (isPublic(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Authentifier l'utilisateur
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Force password change → whitelist stricte
                User user = userDetailsService.getUserByMatricule(userDetails.getUsername());
                if (user != null && user.isMustChangePassword()) {
                    if (!isAllowedDuringForcedChange(path)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Vous devez changer votre mot de passe avant de continuer.");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Erreur d'authentification : {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        return path.startsWith("/api/auth/")
         || path.startsWith("/uploads/")     // ⭐⭐⭐ OBLIGATOIRE
        || "/error".equals(path)
            || "/error".equals(path)
            || path.startsWith("/users/check-matricule")
            || path.startsWith("/users/forgot-password")
            || path.startsWith("/users/reset-password")
            || path.startsWith("/users/validate-reset-token");
    }

    private boolean isAllowedDuringForcedChange(String path) {
        // ✅ autorisés pendant mustChangePassword
        return path.startsWith("/users/change-password")
            || path.startsWith("/users/verify-old-password")
            || path.startsWith("/users/forgot-password")
            || path.startsWith("/users/reset-password")
            || path.startsWith("/users/validate-reset-token");
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
