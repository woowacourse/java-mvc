package webmvc.org.springframework.web.servlet.mvc.tobe;

import web.org.springframework.web.bind.annotation.RequestMethod;

public class Request {

    private final String url;

    private final RequestMethod method;

    public Request(final String uri, final String method) {
        this.url = uri;
        this.method = RequestMethod.get(method);
    }

    public String getUri() {
        return url;
    }

    public RequestMethod getMethod() {
        return method;
    }
}
