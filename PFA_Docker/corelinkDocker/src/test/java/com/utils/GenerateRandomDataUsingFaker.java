package com.utils;

import net.datafaker.Faker;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

public class GenerateRandomDataUsingFaker {

    private static final Faker F = new Faker(new Locale("fr"));
    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // --- détection de placeholder uniquement ---
    public static boolean isPlaceholder(String v) {
        if (v == null) return false;
        String s = v.trim();
        return s.equalsIgnoreCase("<auto>") || s.equalsIgnoreCase("<faker>");
    }

    // --- générateurs unitaires ---
    //pour la gestion des users
    public static String prenom()      { return F.name().firstName(); }
    public static String nom()         { return F.name().lastName(); }
    public static String matricule()   { return "u" + F.number().digits(8); }
    public static String password()    { return "A" + F.letterify("?????") + F.numerify("##") + "@"; }
    public static String role()        { return "MEMBER"; }
    public static String email(String p, String n) {
        String first = (p == null || p.isBlank()) ? prenom() : p;
        String last  = (n == null || n.isBlank()) ? nom()    : n;
        String base  = (first + "." + last + F.number().digits(3) + "@example.com");
        return base.toLowerCase().replace(" ", "");
    }
   
    //pour la gestion des actualités
    //titre unique fel les actualités+ na7iw les apostrophes
    public static String titre() { return F.book().title().replace("'", "") + "#" + java.util.UUID.randomUUID(); }
    public static String description() { return F.lorem().sentence(9); }

    
    //--- gestion des dates :projets et taches ---
    //createProjet format fr
    public static String dateDebut() {
        LocalDate today = LocalDate.now();
        LocalDate debut = today.plusDays(F.number().numberBetween(0, 30));
        return debut.format(FR); // => "28/08/2025"
    }

    public static String dateFin(String dateDebutFr) {
        LocalDate debut = LocalDate.parse(dateDebutFr, FR);
        LocalDate fin = debut.plusDays(F.number().numberBetween(1, 180));
        return fin.format(FR); // => "15/12/2025"
    }
    
   


  

    /**
     * Remplit UNIQUEMENT les champs marqués <auto>/<faker>.
     */
    public static Map<String, String> apply(Map<String, String> in) {
        Map<String, String> m = new HashMap<>(in);

        String p = isPlaceholder(m.get("prenom")) ? prenom() : m.get("prenom");
        String n = isPlaceholder(m.get("nom"))    ? nom()    : m.get("nom");
        m.put("prenom", p);
        m.put("nom",    n);

        if (isPlaceholder(m.get("matricule")))   m.put("matricule",   matricule());
        if (isPlaceholder(m.get("email")))       m.put("email",       email(p, n));
        if (isPlaceholder(m.get("password")))    m.put("password",    password());
        if (isPlaceholder(m.get("role")))        m.put("role",        role());
        if (isPlaceholder(m.get("titre")))       m.put("titre",       titre());
        if (isPlaceholder(m.get("description"))) m.put("description", description());
        if (isPlaceholder(m.get("dateDebut"))) m.put("dateDebut", dateDebut());
        if (isPlaceholder(m.get("dateFin"))) {
            String debut = m.get("dateDebut");
            m.put("dateFin", dateFin(debut));
        }

        return m;
    }
}
