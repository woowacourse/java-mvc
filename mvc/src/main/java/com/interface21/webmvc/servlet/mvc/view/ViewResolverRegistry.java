package com.interface21.webmvc.servlet.mvc.view;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.exception.UnsupportedResponseTypeException;
import java.util.List;

public class ViewResolverRegistry {

    private final List<ViewResolver> resolvers;

    public ViewResolverRegistry() {
        this.resolvers = List.of(
                new ModelAndViewConverter(),
                new ViewNameConverter(),
                new ObjectToJsonConverter()
        );
    }

    public ModelAndView resolve(Object result) {
        if (result != null) {
            return resolvers.stream()
                    .filter(resolver -> resolver.canHandle(result))
                    .findFirst()
                    .map(resolver -> resolver.convert(result))
                    .orElseThrow(
                            () -> new UnsupportedResponseTypeException(
                                    "Unsupported response type: " + result.getClass().getName()
                            )
                    );
        }
        throw new UnsupportedResponseTypeException("response type cannot be null");
    }
}
