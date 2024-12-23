package project;

public class Customer extends User {

    public enum Gender {
        MALE, FEMALE;
    }

    private double balance;
    private String address;
    private String interests;
    private Gender gender;

    public Customer(String username, String password, String gender, String dateOfBirth, double balance, String address, String interests) {
        super(username, password, dateOfBirth);
        validateBalance(balance);
        validateAddress(address);

        this.balance = balance;
        this.address = address.trim();
        this.interests = interests != null ? interests.trim() : "";

        try {
            this.gender = Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender. Please use 'MALE' or 'FEMALE'.");
        }
    }

    private void validateBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String getInterests() {
        return interests;
    }

    public Gender getGender() {

        return gender;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.address = address.trim();
    }

    public void setInterests(String interests) {
        this.interests = interests != null ? interests.trim() : "";
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
