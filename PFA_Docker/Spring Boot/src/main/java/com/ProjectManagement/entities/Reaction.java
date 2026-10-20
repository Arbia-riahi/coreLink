package com.ProjectManagement.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    public Reaction() {}

    public Reaction(ReactionType type, User user, Message message) {
        this.type = type;
        this.user = user;
        this.message = message;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ReactionType getType() { return type; }
    public void setType(ReactionType type) { this.type = type; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }
}
