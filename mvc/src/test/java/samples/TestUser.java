package samples;

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
}
