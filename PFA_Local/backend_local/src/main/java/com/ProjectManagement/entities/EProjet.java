package com.ProjectManagement.entities;

public enum EProjet {
    EN_ATTENTE("En attente"),
    EN_COURS("En cours"),
    TERMINE("Terminé"),
    ANNULE("Annulé");

    private final String label;

    EProjet(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}


