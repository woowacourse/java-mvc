package nextstep.web.support;

public enum JspPage {
    INDEX("/index.jsp"),
    LOGIN("/login.jsp"),
    REGISTER("/register.jsp"),
    UNAUTHORIZED("/401.jsp"),
    GET_TEST("/get-test.jsp"),
    POST_TEST("/post-test.jsp");

    private final String page;

    JspPage(final String page) {
        this.page = page;
    }

    public String value() {
        return page;
    }
}
