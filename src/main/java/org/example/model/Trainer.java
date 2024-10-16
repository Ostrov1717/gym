package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Trainer extends User implements Serializable {
    private long userId;
    private TrainingType specialization;

    public Trainer() {
    }

    public Trainer(long userId, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trainer{");
        sb.append("userID=").append(userId);
        sb.append(super.toString());
        sb.append(", specialization=").append(specialization);
        sb.append('}');
        return sb.toString();
    }
}
