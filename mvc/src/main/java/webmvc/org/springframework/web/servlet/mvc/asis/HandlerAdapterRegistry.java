package webmvc.org.springframework.web.servlet.mvc.asis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> getHandlerAdapter(Object object) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.support(object))
            .findAny();
    }
}
