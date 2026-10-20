package com.ProjectManagement.entities;

public enum ReactionType {
    LIKE("👍"),
    
    DONE("✅"),
    PERFECT("💯"),
    CLAP("👏");

    private final String emoji;

    ReactionType(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    public static boolean isValid(String emoji) {
        for (ReactionType type : values()) {
            if (type.getEmoji().equals(emoji)) return true;
        }
        return false;
    }

    /** Ajout de la méthode pour retrouver le type par emoji */
    public static ReactionType fromEmoji(String emoji) {
        for (ReactionType type : values()) {
            if (type.getEmoji().equals(emoji)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Emoji non reconnu : " + emoji);
    }
}
