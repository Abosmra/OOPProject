package project;

public class Admin extends User {

    private int workingHours;
    private String role;

    @SuppressWarnings("FieldMayBeFinal")

    public Admin(String username, String password, String dateOfBirth, String role, int workingHours) {
        super(username, password, dateOfBirth);
        validateWorkingHours(workingHours);
        validateRole(role);
        this.role = role;
        this.workingHours = workingHours;
    }

    private void validateWorkingHours(int workingHours) {
        if (workingHours <= 0) {
            throw new IllegalArgumentException("Working hours must be greater than zero.");
        }
    }

    private void validateRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        validateRole(role);
        this.role = role;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        validateWorkingHours(workingHours);
        this.workingHours = workingHours;
    }

}
