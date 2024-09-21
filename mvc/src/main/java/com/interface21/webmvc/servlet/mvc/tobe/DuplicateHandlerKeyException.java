package com.interface21.webmvc.servlet.mvc.tobe;

public class DuplicateHandlerKeyException extends RuntimeException {
    public DuplicateHandlerKeyException(final HandlerKey key) {
        super(String.format("%s 는 이미 선언이 되었습니다.", key));
    }
}
