package com.ProjectManagement.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ProjectManagement.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

/**
 * Utilitaire pour la gestion des tokens JWT dans l'application.
 * Ce composant génère, valide et extrait des informations des tokens JWT utilisés pour l'authentification.
 */
@Component
public class JwtUtils {

    // Logger pour enregistrer les erreurs et les informations de débogage
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Clé secrète utilisée pour signer et valider le JWT, injectée depuis le fichier de configuration
    @Value("${jwtSecret}")
    private String jwtSecret;

    // Temps d'expiration du token en millisecondes, injecté depuis le fichier de configuration
    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Génère un token JWT pour l'utilisateur authentifié.
     * Le token contient l'identifiant de l'utilisateur, la date d'émission et la date d'expiration.
     *
     * @param authentication L'objet d'authentification contenant les détails de l'utilisateur.
     * @return Le token JWT généré sous forme de chaîne de caractères.
     */
    public String generateJwtToken(Authentication authentication) {
        // Récupération des informations de l'utilisateur principal à partir de l'objet d'authentification
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Création du token JWT avec les informations nécessaires
        return Jwts.builder()
            .setSubject(userPrincipal.getUsername()) // Définir le sujet du token (nom de l'utilisateur)
            .setIssuedAt(new Date()) // Définir la date d'émission du token
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Définir la date d'expiration
            .signWith(key(), SignatureAlgorithm.HS256) // Signer le token avec la clé secrète et l'algorithme HS256
            .compact(); // Générer le token compacté
    }

    /**
     * Extrait et retourne la clé secrète sous forme de clé cryptographique utilisée pour signer le JWT.
     *
     * @return La clé secrète en format clé cryptographique.
     */
    private Key key() {
        // Convertir la clé secrète en un tableau d'octets et générer la clé cryptographique
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Extrait le nom d'utilisateur du token JWT.
     * Le nom d'utilisateur est stocké sous forme de "subject" dans le token JWT.
     *
     * @param token Le token JWT à partir duquel extraire le nom d'utilisateur.
     * @return Le nom d'utilisateur extrait du token.
     */
    public String getUserNameFromJwtToken(String token) {
        // Extraire le nom d'utilisateur (subject) du token JWT validé
        return Jwts.parserBuilder()
            .setSigningKey(key()) // Spécifier la clé utilisée pour valider le token
            .build()
            .parseClaimsJws(token) // Analyser le JWT et récupérer les claims
            .getBody() // Extraire le corps du JWT
            .getSubject(); // Retourner le nom d'utilisateur (subject)
    }

    /**
     * Valide le token JWT en vérifiant sa signature, son expiration et sa conformité.
     *
     * @param authToken Le token JWT à valider.
     * @return true si le token est valide, false sinon.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // Tenter de valider le token en utilisant la clé secrète
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true; // Le token est valide si aucune exception n'est levée
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage()); // Log si le token est malformé
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage()); // Log si le token est expiré
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage()); // Log si le token utilise un algorithme non supporté
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage()); // Log si les informations du token sont vides
        }

        return false; // Retourner false si le token n'est pas valide
    }
}
