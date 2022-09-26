package nextstep.mvc.adapter;

public class AdapterNotFoundException extends RuntimeException {

    private static final String MESSAGE = "어댑터가 존재하지 않습니다.";

    public AdapterNotFoundException() {
        super(MESSAGE);
    }
}
