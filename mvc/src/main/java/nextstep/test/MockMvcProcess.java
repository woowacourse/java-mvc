package nextstep.test;

public class MockMvcProcess {

    private final RequestInfo requestInfo;
    private final MockDispatcherServlet dispatcherServlet;

    public MockMvcProcess(RequestInfo requestInfo, MockDispatcherServlet dispatcherServlet) {
        this.requestInfo = requestInfo;
        this.dispatcherServlet = dispatcherServlet;
    }

    public MockMvcResponse result() {
        return dispatcherServlet.doService(requestInfo);
    }
}
