package nextstep.mvc.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import com.techcourse.air.mvc.core.adapter.SimpleControllerHandlerAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.techcourse.air.mvc.core.view.ModelAndView;
import samples.TestInterfaceController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SimpleControllerHandlerAdapterTest {

    private SimpleControllerHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new ApplicationContext("samples");
        ApplicationContextProvider.setApplicationContext(context);
        context.initializeContext();
        handlerAdapter = context.findBeanByType(SimpleControllerHandlerAdapter.class);
    }

    @Test
    @DisplayName("해당 어댑터가 처리할 수 있는지 확인")
    void supports() {
        // given
        TestInterfaceController handler = new TestInterfaceController();

        // when
        boolean supports = handlerAdapter.supports(handler);

        // then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("해당 어댑터로 핸들러 처리하기")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        TestInterfaceController handler = new TestInterfaceController();

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getView()).usingRecursiveComparison()
                                          .isEqualTo("/login.jsp");
    }
}
