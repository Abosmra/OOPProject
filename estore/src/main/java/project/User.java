package project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class User {

    protected String username;
    protected String password;
    protected Date dateOfBirth;

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy");

    public User(String username, String password, String dateOfBirth) {
        validateUsername(username);
        validatePassword(password);
        validateDateOfBirth(dateOfBirth);

        this.username = username;
        this.password = password;
        this.dateOfBirth = parseDate(dateOfBirth);
    }

    public String getDateOfBirth() {
        return DATE_FORMAT.format(dateOfBirth);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        validateUsername(username);
        this.username = username;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public void setDateOfBirth(String dateOfBirth) {
        validateDateOfBirth(dateOfBirth);
        this.dateOfBirth = parseDate(dateOfBirth);
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
    }

    private void validateDateOfBirth(String dateOfBirth) {
      if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
          throw new IllegalArgumentException("Date of Birth cannot be null or empty.");
      }
      if (!isValidDate(dateOfBirth)) {
          throw new IllegalArgumentException("Invalid date format. Use dd-MM-yy.");
      }
  }
  
  private boolean isValidDate(String date) {
      try {
          DATE_FORMAT.setLenient(false); // Ensure strict parsing
          DATE_FORMAT.parse(date);
          return true;
      } catch (ParseException e) {
          return false;
      }
  }
  
  private Date parseDate(String dateOfBirth) {
      try {
          DATE_FORMAT.setLenient(false); // Ensure strict parsing
          return DATE_FORMAT.parse(dateOfBirth);
      } catch (ParseException e) {
          throw new IllegalArgumentException("Invalid date format. Use dd-MM-yy.");
      }
  }
  
    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", dateOfBirth='" + getDateOfBirth() + '\''
                + '}';
    }

}
