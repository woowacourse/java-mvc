package nextstep.test;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import nextstep.web.support.RequestMethod;

public class RequestInfo {

    private RequestMethod requestMethod;
    private String path;

    public void addHttpMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void addUrl(String path) {
        this.path = path;
    }

    public String requestMethodName() {
        return requestMethod.name();
    }

    public String path() {
        return path;
    }

    public HttpServletRequest toRequest() {
        final HttpRequestProxyHandler httpRequestProxyHandler = new HttpRequestProxyHandler();
        httpRequestProxyHandler.setRequestInfo(this);
        return (HttpServletRequest) Proxy.newProxyInstance(
            HttpServletRequest.class.getClassLoader(),
            new Class<?>[]{HttpServletRequest.class},
            httpRequestProxyHandler);
    }
}
