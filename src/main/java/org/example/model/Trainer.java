package org.example.model;

public class Trainer extends User{
    private long userId;
    private TrainingType specialization;

    public Trainer(){}

    public Trainer(long userId,String firstName, String lastName, String username, String password, boolean isActive,  TrainingType specialization) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Trainer trainer = (Trainer) o;

        if (userId != trainer.userId) return false;
        return specialization == trainer.specialization;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (specialization != null ? specialization.hashCode() : 0);
        return result;
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
