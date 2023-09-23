package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerAdapter;

public class InvalidHandlerForHandlerAdapterException extends RuntimeException {

    public InvalidHandlerForHandlerAdapterException(final Object handler,
        final HandlerAdapter adapter) {
        super(String.format(
                "Adapter can not handle handler. ( Adapter : %s, Handler : %s )",
                handler.toString(),
                adapter.toString()
            )
        );
    }
}
