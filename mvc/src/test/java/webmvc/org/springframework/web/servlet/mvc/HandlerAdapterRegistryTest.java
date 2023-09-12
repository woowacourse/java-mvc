package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.UnsupportedHandlerException;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerAdapterRegistryTest {

    @Test
    void HandlerAdapter에_등록한_타입에_맞는_Adapter를_가져온다() throws Exception {
        var handlerAdapterRegistry = new HandlerAdapterRegistry();

        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(controllerHandlerAdapter);

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new Controller() {
            @Override
            public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                return null;
            }
        });

        assertThat(handlerAdapter).isEqualTo(controllerHandlerAdapter);
    }

    @Test
    void HandlerAdapter에_등록되어_있지_않는_객체면_예외가_발생한다() {
        var handlerAdapterRegistry = new HandlerAdapterRegistry();

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(""))
                .isInstanceOf(UnsupportedHandlerException.class);
    }
}
