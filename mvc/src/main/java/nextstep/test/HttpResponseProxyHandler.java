package nextstep.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import nextstep.web.support.StatusCode;

public class HttpResponseProxyHandler implements InvocationHandler {

    private MockMvcResponse mockMvcResponse;

    public HttpResponseProxyHandler(MockMvcResponse mockMvcResponse) {
        this.mockMvcResponse = mockMvcResponse;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("setStatus")) {
            resolveStatus(args);
            return null;
        }

        if (method.getName().equals("setHeader")) {
            resolveHeader(args);
            return null;
        }

        if (method.getName().equals("getWriter")) {
            final FakeWriter writer = new FakeWriter();
            mockMvcResponse.addWriter(writer);
            return writer;
        }

        if (method.getName().equals("flushBuffer")) {
            return null;
        }

        return method.invoke(proxy, args);
    }

    private void resolveStatus(Object[] args) {
        for (Object arg : args) {
            final StatusCode statusCode = StatusCode.findByNumber((int) arg);
            mockMvcResponse.setStatusCode(statusCode);
        }
    }

    private void resolveHeader(Object[] args) {
        final String key = (String) args[0];
        final String value = (String) args[1];
        mockMvcResponse.addHeader(key, value);
    }

    public MockMvcResponse getResponse() {
        return mockMvcResponse;
    }
}
