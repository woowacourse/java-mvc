package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.exception.DispatcherServletException;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerKeyComposite;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapterComposite;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingComposite;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingComposite handlerMappingComposite;
    private HandlerAdapterComposite handlerAdapterComposite;

    @Override
    public void init() {
        handlerMappingComposite = new HandlerMappingComposite();
        handlerAdapterComposite = new HandlerAdapterComposite();

        handlerMappingComposite.addHandlerMapping(new AnnotationHandlerMapping(new AnnotationHandlerKeyComposite()));
        handlerMappingComposite.addHandlerMapping(new ManualHandlerMapping());

        handlerAdapterComposite.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterComposite.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        logRequest(request);

        final Optional<HandlerMapping> maybeHandlerMapping = handlerMappingComposite.getHandlerMapping(request);
        if (maybeHandlerMapping.isEmpty()) {
            throw new DispatcherServletException("[ERROR] DispatcherServlet 에서 현재 요청에 맞는 HandlerMapping 을 찾던 도중에 오류가 발생하였습니다.");
        }

        final HandlerMapping handlerMapping = maybeHandlerMapping.get();
        final Object handlerExecution = handlerMapping.getHandlerExecution(request);
        final ModelAndView mv = handlerAdapterComposite.doService(request, response, handlerExecution);

        try {
            mv.getView().render(mv.getModel(), request, response);
        } catch (Exception e) {
            log.warn("렌더링 하던 도중에 오류가 발생하였습니다.", e);
            throw new DispatcherServletException("[ERROR] 디스패처 서블릿에서 렌더링하던 도중에 오류가 발생하였습니다.");
        }

        logResponse(response);
    }

    private void logRequest(final HttpServletRequest request) {
        log.info("======> Request Method   : {}", request.getMethod());
        log.info("======> Request Url      : {}", request.getRequestURL());
        log.info("======> Request Cookies  : {}", Arrays.stream(request.getCookies())
                .map(cookie -> cookie.getName() + ": " + cookie.getValue())
                .collect(Collectors.toList()));
    }

    private void logResponse(final HttpServletResponse response) {
        log.info("======> Response Status   : {}", response.getStatus());
        log.info("======> Response HeaderNames      : {}", response.getHeaderNames());
    }
}
