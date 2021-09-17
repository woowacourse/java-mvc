package nextstep.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Proxy;
import nextstep.core.ApplicationContext;
import nextstep.mvc.DispatcherServlet;

public class MockDispatcherServlet extends DispatcherServlet {

    public MockDispatcherServlet(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    public MockMvcResponse doService(RequestInfo requestInfo) {
        try {
            final MockMvcResponse mockMvcResponse = new MockMvcResponse();
            final HttpResponseProxyHandler responseHandler = new HttpResponseProxyHandler(
                mockMvcResponse);

            final HttpServletRequest httpServletRequest = requestInfo.toRequest();
            final HttpServletResponse httpServletResponse = responseProxy(responseHandler);

            service(httpServletRequest, httpServletResponse);
            return responseHandler.getResponse();
        } catch (ServletException e) {
            e.printStackTrace();
            throw new IllegalStateException("can not resolve service");
        }
    }

    private HttpServletResponse responseProxy(HttpResponseProxyHandler responseHandler) {
        return (HttpServletResponse) Proxy
            .newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class<?>[]{HttpServletResponse.class},
                responseHandler);
    }
}
