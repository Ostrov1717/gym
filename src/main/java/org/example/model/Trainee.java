package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User {
    private long userId;
    private String address;
    private LocalDate dateOfBirth;

    public Trainee() {
    }

    public Trainee(long userId, String firstName, String lastName, String username, String password, boolean isActive, String address, LocalDate dateOfBirth) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trainee{");
        sb.append("userId=").append(userId);
        sb.append(super.toString());
        sb.append(", address='").append(address).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append('}');
        return sb.toString();
    }
}
