package nextstep.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;

public class MockMvc {

    private final DispatcherServlet dispatcherServlet;

    public MockMvc(final HandlerMapping handlerMapping, final AnnotationHandlerAdapter handlerAdapter) {
        this.dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping);
        dispatcherServlet.addHandlerAdapter(handlerAdapter);
        dispatcherServlet.init();
    }

    public ResultMatcher perform(final MockRequestBuilder builder) throws Exception {
        final HttpServletRequest request = builder.build();
        final HttpServletResponse response = createMockedResponse();

        dispatcherServlet.service(request, response);

        return new ResultMatcher(request, response);
    }

    private HttpServletResponse createMockedResponse() throws Exception {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        return response;
    }
}
