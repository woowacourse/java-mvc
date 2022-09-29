package samples;

public class TUser {

    private Long id;
    private String account;

    public TUser(final Long id, final String account) {
        this.id = id;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }
}
