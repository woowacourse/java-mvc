package samples;

import java.util.Objects;

public class TestUser {

    private String account;
    private String password;
    private String email;

    public TestUser() {
    }

    public TestUser(String account, String password, String email) {
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestUser testUser = (TestUser) o;
        return Objects.equals(account, testUser.account) && Objects.equals(password, testUser.password) && Objects.equals(email, testUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, password, email);
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
