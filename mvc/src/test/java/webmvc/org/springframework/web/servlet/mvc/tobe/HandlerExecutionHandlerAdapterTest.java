package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerExecutionHandlerAdapterTest {

    @Test
    void HandlerExecution_구현체는_true를_반환한다() {
        HandlerAdapter controllerHandlerAdapter = new HandlerExecutionHandlerAdapter();

        boolean expect = controllerHandlerAdapter.isSupport(new HandlerExecution(null, null));

        assertThat(expect).isTrue();
    }

    @Test
    void Controller_구현체는_false를_반환한다() {
        HandlerAdapter controllerHandlerAdapter = new HandlerExecutionHandlerAdapter();
        var controller = new ForwardController("");

        boolean expect = controllerHandlerAdapter.isSupport(controller);

        assertThat(expect).isFalse();
    }
}
