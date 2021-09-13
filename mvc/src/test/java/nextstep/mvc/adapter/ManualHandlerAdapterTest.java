package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class ManualHandlerAdapterTest {

    @DisplayName("ManualHandlerAdapter 는 Controller 를 지원한다.")
    @Test
    void supports() {
        // given
        Controller controller = mock(Controller.class);

        // when, then
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        assertThat(manualHandlerAdapter.supports(controller)).isTrue();
    }

    @DisplayName("ManualHandlerAdapter 로 컨트롤러를 실행한다.")
    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        manualHandlerAdapter.handle(request, response, controller);

        // then
        then(controller).should(times(1))
                .execute(request, response);
    }
}
