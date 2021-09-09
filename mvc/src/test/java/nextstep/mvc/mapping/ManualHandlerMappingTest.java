package nextstep.mvc.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.techcourse.air.mvc.core.controller.asis.Controller;
import com.techcourse.air.mvc.core.mapping.ManualHandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new ApplicationContext("samples");
        ApplicationContextProvider.setApplicationContext(context);
        context.initializeContext();
        handlerMapping = context.findBeanByType(ManualHandlerMapping.class);
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("기존 컨트롤러를 처리하는 ManualHandlerMapping 테스트")
    void login() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");
        final Controller controller = handlerMapping.getHandler(request);
        String result = controller.execute(request, response);

        // then
        assertThat(result).isEqualTo("/login.jsp");
    }
}
