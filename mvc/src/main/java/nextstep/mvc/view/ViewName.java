package nextstep.mvc.view;

public enum ViewName {
    REDIRECT("redirect:"),
    HOME("/"),
    REDIRECT_HOME(REDIRECT.url + HOME.url),
    EMPTY(""),
    LOGIN("/login.jsp"),
    REDIRECT_LOGIN(REDIRECT.url + LOGIN.url),
    REGISTER("/register.jsp"),
    REDIRECT_REGISTER(REDIRECT.url + REGISTER.url),
    INDEX("/index.jsp"),
    REDIRECT_INDEX(REDIRECT.url + INDEX.url),
    BAD_REQUEST("/400.jsp"),
    REDIRECT_BADREQUEST(REDIRECT.url + BAD_REQUEST.url),
    UNAUTHORIZED("/401.jsp"),
    REDIRECT_UNAUTHORIZED(REDIRECT.url + UNAUTHORIZED.url),
    ;

    private final String url;

    ViewName(String url) {
        this.url = url;
    }

    public boolean startsWith(String prefix) {
        return url.startsWith(prefix);
    }

    public String substring(int length) {
        return url.substring(length);
    }

    public String url() {
        return url;
    }
}
