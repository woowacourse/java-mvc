package nextstep.mvc;

import nextstep.mvc.controller.ControllerContainer;

public interface WebMvcConfigurer {

    default void addController(ControllerContainer controllerContainer){}
}
