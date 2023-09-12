package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdaptorRegistry handlerAdaptorRegistry;

    public void addHandlerAdaptor(HandlerAdaptor handlerAdaptor) {
        this.handlerAdaptorRegistry.addHandlerAdaptor(handlerAdaptor);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        this.handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        final Optional<Object> handler = handlerMappingRegistry.getHandler(req);
        if (handler.isEmpty()) {
            throw new IllegalArgumentException("No Handler Matched");
        }

        final HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler.get());

        ModelAndView modelAndView = null;
        try {
            modelAndView = handlerAdaptor.handle(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: ExceptionHandler 구현하기
        }

        if (Objects.nonNull(modelAndView)) {
            render(modelAndView, req, resp);
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest req, final HttpServletResponse resp) {
        // TODO: 뷰 구현하기
    }
}
