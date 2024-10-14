package com.interface21.webmvc.servlet.mvc;

public class CantRenderException extends RuntimeException {
    public CantRenderException(Throwable e) {
        super(e);
    }
}
