package nextstep.mvc.controller;

public class ResponseEntity<T> {

    private final T value;

    public ResponseEntity(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
