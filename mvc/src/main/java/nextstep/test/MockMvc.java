package nextstep.test;

import nextstep.core.ApplicationContext;
import nextstep.web.support.RequestMethod;

public class MockMvc {

    private final MockDispatcherServlet dispatcherServlet;

    public MockMvc(ApplicationContext applicationContext) {
        this.dispatcherServlet = new MockDispatcherServlet(applicationContext);
        dispatcherServlet.init();
    }

    public MockMvcProcess get(String path) {
        final RequestInfo requestInfo = new RequestInfo();
        requestInfo.addHttpMethod(RequestMethod.GET);
        requestInfo.addUrl(path);

        return new MockMvcProcess(requestInfo, dispatcherServlet);
    }
}
