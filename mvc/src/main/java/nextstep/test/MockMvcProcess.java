package nextstep.test;

import java.util.Map;

public class MockMvcProcess {

    private final RequestInfo requestInfo;
    private final MockDispatcherServlet dispatcherServlet;

    public MockMvcProcess(RequestInfo requestInfo, MockDispatcherServlet dispatcherServlet) {
        this.requestInfo = requestInfo;
        this.dispatcherServlet = dispatcherServlet;
    }

    public MockMvcProcess withParams(Map<String, String> params) {
        requestInfo.addParams(params);
        return this;
    }

    public MockMvcProcess withParams(String key, String value) {
        requestInfo.addParams(key, value);
        return this;
    }

    public MockMvcResponse result() {
        return dispatcherServlet.doService(requestInfo);
    }
}
