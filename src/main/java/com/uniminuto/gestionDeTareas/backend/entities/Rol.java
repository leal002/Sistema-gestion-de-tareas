package com.uniminuto.gestionDeTareas.backend.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Rol {
    QUALITY_ANALYST("Quality Analyst"),
    TEST_DEVELOPER("Test Developer"),
    QUALITY_MANAGER("Quality manager"),
    PRODUCT_MANAGER("Product Manager"),
    SCRUM_MASTER("Scrum Master"),
    SCRUM_TEAM("Scrum Team");

    private final String displayName;

    Rol(String displayName){
        this.displayName = displayName;
    
    }

    @JsonValue
    public String getDisplayName(){
        return displayName;
    }
}
