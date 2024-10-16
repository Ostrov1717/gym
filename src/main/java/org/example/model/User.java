package org.example.model;

import lombok.Data;

import java.io.Serializable;

@Data
abstract class User implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;

    public User() {
    }

    public User(String firstName, String lastName, String username, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = isActive;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", isActive=").append(active);
        return sb.toString();
    }
}
