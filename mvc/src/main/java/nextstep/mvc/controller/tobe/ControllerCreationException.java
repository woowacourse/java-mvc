package nextstep.mvc.controller.tobe;

public class ControllerCreationException extends RuntimeException {

    public ControllerCreationException(String className) {
        super(String.format("Error while creating controller %s for handler mapping process", className));
    }
}
