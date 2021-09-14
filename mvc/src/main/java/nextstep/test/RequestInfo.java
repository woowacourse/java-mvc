package nextstep.test;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import nextstep.web.support.RequestMethod;

public class RequestInfo {

    private RequestMethod requestMethod;
    private String path;
    private Map<String, String> params = new HashMap<>();

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

    public void addParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    public void addParams(String key, String value) {
        this.params.put(key, value);
    }

    public Object getParam(String key) {
        return params.get(key);
    }
}
