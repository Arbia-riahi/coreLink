package com.ProjectManagement.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Cette classe est le point d'entrée pour les erreurs d'authentification.
 * Elle est utilisée pour gérer les erreurs d'authentification (par exemple, token manquant ou invalide)
 * en envoyant une réponse détaillée au client.
 * Elle implémente l'interface AuthenticationEntryPoint de Spring Security.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    // Logger pour l'affichage des erreurs dans la console
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Cette méthode est appelée quand une erreur d'authentification survient.
     * Elle envoie une réponse JSON avec des détails sur l'erreur d'authentification.
     *
     * @param request L'objet HttpServletRequest contenant la requête HTTP.
     * @param response L'objet HttpServletResponse permettant de renvoyer la réponse au client.
     * @param authException L'exception d'authentification qui a causé l'erreur.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     * @throws ServletException Si un problème survient lors de l'exécution du servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Log l'erreur d'authentification dans les logs du serveur
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Définir le type de contenu de la réponse comme JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Définir le statut HTTP 401 (Non autorisé)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Création d'une carte contenant les informations à renvoyer au client
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);  // Statut HTTP
        body.put("error", "Unauthorized");                        // Type d'erreur
        body.put("message", authException.getMessage());          // Message d'erreur détaillé
        body.put("path", request.getServletPath());               // URL qui a provoqué l'erreur

        // Utilisation d'ObjectMapper pour transformer l'objet Java en JSON
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);      // Renvoyer la réponse JSON
    }
}
