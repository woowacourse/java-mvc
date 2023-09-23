package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.exception.HandlerAdapterException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterComposite {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapterComposite.class);

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter newHandlerAdapter) {
        handlerAdapters.add(newHandlerAdapter);
    }

    public ModelAndView doService(final HttpServletRequest request,
                                  final HttpServletResponse response,
                                  final Object method
    ) {
        return (ModelAndView) handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(method))
                .findFirst()
                .map(handlerAdapter -> doHandle(request, response, method, handlerAdapter))
                .orElse(null);
    }

    private Object doHandle(final HttpServletRequest request,
                            final HttpServletResponse response,
                            final Object method,
                            final HandlerAdapter handlerAdapter
    ) {
        try {
            return handlerAdapter.doInternalService(request, response, method);
        } catch (Exception e) {
            log.warn("핸들러 어댑터 컴포시트에서 핸들러 어댑터 내부에서 로직을 수행하던 도중 오류가 발생하였습니다.", e);
            throw new HandlerAdapterException("[ERROR] 핸들러 어댑터 내부 로직을 수행하던 도중 오류가 발생하였습니다.");
        }
    }
}
