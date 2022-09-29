package samples;

public class TestUser {

    private final long id;
    private final String account;

    public TestUser(final long id, final String account) {
        this.id = id;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

}
