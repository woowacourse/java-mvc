package com.interface21.webmvc.servlet.view.impl;

public class JsonViewRenderFailedException extends RuntimeException {

    public JsonViewRenderFailedException(Throwable cause) {
        super("JSON 뷰를 렌더링하는데에 실패했습니다.", cause);
    }
}
