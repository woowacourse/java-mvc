package samples;

public class DummyUser {

    private long id;
    private String account;
    private String password;
    private String email;

    public static DummyUser create() {
        DummyUser user = new DummyUser();
        user.id = 1L;
        user.account = "ggyool";
        user.password = "password";
        user.email = "ggyool@email.com";
        return user;
    }
}
