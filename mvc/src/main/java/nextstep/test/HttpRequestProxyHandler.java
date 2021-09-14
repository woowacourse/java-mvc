package nextstep.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpRequestProxyHandler implements InvocationHandler {

    private RequestInfo requestInfo;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getMethod")) {
            return requestInfo.requestMethodName();
        }

        if (method.getName().equals("getRequestURI")) {
            return requestInfo.path();
        }

        return method.invoke(proxy, args);
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
