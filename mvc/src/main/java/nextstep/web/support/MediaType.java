package nextstep.web.support;

public enum MediaType {
    APPLICATION_JSON_UTF8_VALUE("application/json;charset=UTF-8"),
    HTML("text/html;charset=utf-8"),
    CSS("text/css"),
    JS("application/javascript"),
    SVG("image/svg+xml");

    private final String contentType;

    MediaType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
