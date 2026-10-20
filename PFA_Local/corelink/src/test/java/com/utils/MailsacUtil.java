package com.utils;

import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Pattern;

public class MailsacUtil {

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    /**
     * Essaie de récupérer le lien de réinitialisation Mailsac.
     * Retente plusieurs fois si aucun message n'est trouvé (jusqu’à 5 fois).
     */
    public static Optional<String> getResetLink(String apiKey, String email) {
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        int maxRetries = 5;
        Duration wait = Duration.ofSeconds(2);

        for (int i = 0; i < maxRetries; i++) {
            try {
                var req = HttpRequest.newBuilder()
                        .uri(URI.create("https://mailsac.com/api/addresses/" + encodedEmail + "/messages"))
                        .header("Mailsac-Key", apiKey)
                        .GET().build();

                var res = client.send(req, HttpResponse.BodyHandlers.ofString());
                var idMatcher = Pattern.compile("\"_id\"\\s*:\\s*\"([^\"]+)\"").matcher(res.body());
                if (idMatcher.find()) {
                    var msgId = idMatcher.group(1);
                    var textRes = client.send(
                            HttpRequest.newBuilder()
                                    .uri(URI.create("https://mailsac.com/api/text/" + encodedEmail + "/" + msgId))
                                    .header("Mailsac-Key", apiKey)
                                    .GET().build(),
                            HttpResponse.BodyHandlers.ofString());

                    var link = Pattern.compile("https?://\\S+").matcher(textRes.body());
                    if (link.find()) return Optional.of(link.group());
                }
                wait.toMillis();
            } catch (Exception ignored) {
            	System.out.println("ignored");
            }
        }
        return Optional.empty();
    }

    
    
    
    
}


