package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.exception.BadRequestException;
import com.interface21.webmvc.servlet.mvc.exception.InternalServerError;
import com.interface21.webmvc.servlet.mvc.exception.NotFoundException;
import com.interface21.webmvc.servlet.mvc.exception.UnauthorizedException;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private final Object[] basePackage;

    public DispatcherServlet(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
        addHandlerMapping(annotationHandlerMapping);

        addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = getHandler(request);
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView mav = handlerAdapter.handle(request, response, handler);
            render(request, response, mav);
        } catch (BadRequestException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (UnauthorizedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (InternalServerError e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    private Object getHandler(final HttpServletRequest request) throws NotFoundException {
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
        if (handler.isEmpty()) {
            throw new NotFoundException("요청을 처리할 핸들러를 찾지 못했습니다. request URI: " + request.getRequestURI());
        }
        return handler.get();
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) throws InternalServerError {
        final Optional<HandlerAdapter> handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        if (handlerAdapter.isEmpty()) {
            throw new InternalServerError("핸들러 어댑터를 찾지 못했습니다.");
        }
        return handlerAdapter.get();
    }

    private void render(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final ModelAndView modelAndView
    ) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
