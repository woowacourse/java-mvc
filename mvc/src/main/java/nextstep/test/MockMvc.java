package nextstep.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import nextstep.mvc.AnnotationHandlerAdapter;
import nextstep.mvc.ControllerHandlerAdapter;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;

public class MockMvc {

    private final DispatcherServlet dispatcherServlet;

    public MockMvc(final HandlerMapping... handlerMappings) {
        this.dispatcherServlet = new DispatcherServlet();

        for (HandlerMapping handlerMapping : handlerMappings) {
            dispatcherServlet.addHandlerMapping(handlerMapping);
        }
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping());

        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
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
