package entities;

public abstract class User {
    private String UserName;
    private String Password;
    private String DateOfBirth;
    public User(){}
    public User(String UserName, String Password, String DateOfBirth) {
        if (UserName == null || UserName.isEmpty()) {
            throw new IllegalArgumentException("Username is not valid");
        }
        this.UserName = UserName;

        if (Password == null || Password.length() < 8) {
            throw new IllegalArgumentException("Password is not valid (must be 8 or more characters)");
        }
        this.Password = Password;

        if (DateOfBirth == null || DateOfBirth.isEmpty()) {
            throw new IllegalArgumentException("Date of birth is not valid");
        }
        this.DateOfBirth = DateOfBirth;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        if (UserName == null || UserName.isEmpty()) {
            throw new IllegalArgumentException("User name is not valid");
        }
        this.UserName = UserName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        if (Password == null || Password.length() < 8) {
            throw new IllegalArgumentException("Password is not valid (must be 8 or more characters)");
        }
        this.Password = Password;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        if (DateOfBirth == null || DateOfBirth.isEmpty()) {
            throw new IllegalArgumentException("Date of birth is not valid");
        }
        this.DateOfBirth = DateOfBirth;
    }
    @Override
    public String toString() {
        return "User{" + "UserName=" + UserName + ", Password=****" + ", DateOfBirth=" + DateOfBirth + '}';
    }

}