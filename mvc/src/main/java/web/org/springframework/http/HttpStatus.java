package web.org.springframework.http;

public enum HttpStatus {

    NOT_FOUND(404);

    private final int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
