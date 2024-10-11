package org.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

class HashCodeEqualsTest {

    @Test
    @DisplayName("HashCode and Equals methods successfully tested")
    void testHashCodeEquals() {
        EqualsVerifier.simple().forClasses(User.class,Trainee.class,Trainer.class,Training.class).verify();
    }
}
