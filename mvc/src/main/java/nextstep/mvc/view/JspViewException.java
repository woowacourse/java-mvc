package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletResponse;

public enum JspViewException {

    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "/500.jsp"),
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "/404.jsp");

    private final int statusCode;
    private final String view;

    JspViewException(int statusCode, String view) {
        this.statusCode = statusCode;
        this.view = view;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getView() {
        return view;
    }
}
