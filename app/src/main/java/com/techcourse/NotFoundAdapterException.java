package com.techcourse;

public class NotFoundAdapterException extends RuntimeException {

    public NotFoundAdapterException() {
        super("핸들러를 처리할 수 있는 어댑터가 존재하지 않습니다.");
    }
}
