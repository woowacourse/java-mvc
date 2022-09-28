package support;

public class User {
    public Long id;
    public String account;
    public String email;

    public User(final Long id, final String account, final String email) {
        this.id = id;
        this.account = account;
        this.email = email;
    }
}
