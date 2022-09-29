package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ForwardControllerTest {

    @Test
    @DisplayName("/ 로 접근했을때 index.jsp페이지 렌더")
    void execute() throws NoSuchFieldException, IllegalAccessException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        ForwardController forwardController = new ForwardController();

        ModelAndView modelAndView = forwardController.execute(request, response);
        View view = modelAndView.getView();

        Class<? extends View> aClass = view.getClass();
        Field field = aClass.getDeclaredField("viewName");
        field.setAccessible(true);

        assertThat(field.get(view)).isEqualTo("index.jsp");
    }
}
