package webmvc.org.springframework.web.servlet.mvc.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;

public class HandlerExceptionResolvers {

    private static final NotSupportExceptionResolver NOT_SUPPORT_EXCEPTION_RESOLVER = new NotSupportExceptionResolver();

    private final Map<Class<? extends Exception>, HandlerExceptionResolver> handlerExceptionResolverMap = new HashMap<>();

    public void addHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        handlerExceptionResolverMap.put(handlerExceptionResolver.supportException(), handlerExceptionResolver);
    }

    @Nonnull
    public HandlerExceptionResolver getExceptionResolver(Exception ex) {
        return handlerExceptionResolverMap.getOrDefault(ex.getClass(), NOT_SUPPORT_EXCEPTION_RESOLVER);
    }

    private static class NotSupportExceptionResolver implements HandlerExceptionResolver {

        @Override
        public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Exception ex)
            throws Exception {
            throw ex;
        }

        @Override
        public Class<? extends Exception> supportException() {
            throw new UnsupportedOperationException("");
        }
    }
}
