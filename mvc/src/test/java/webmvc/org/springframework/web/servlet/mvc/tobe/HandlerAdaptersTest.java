package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

class HandlerAdaptersTest {

  private HandlerAdapter handlerAdapters;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @BeforeEach
  void setUp() {
    handlerAdapters = new HandlerAdapters(
        List.of(
            new ManualHandlerAdapter(),
            new AnnotationHandlerAdapter()
        )
    );

    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
  }

  @Test
  @DisplayName("handle() : Controller Interface의 adapter를 찾아 실행할 수 있다.")
  void test_handle_controller_interface() throws Exception {
    //given
    Controller controller = new ForwardController("path");

    //when
    final Object handle = handlerAdapters.handle(request, response, controller);

    //then
    assertThat(handle).isInstanceOf(String.class);
  }

  @Test
  @DisplayName("handle() : 어노테이션 기반의 adapter를 찾아 실행할 수 있다.")
  void test_handle_annotation() throws Exception {
    //given
    final TestController controller = new TestController();
    final HandlerExecution handlerExecution = new HandlerExecution(controller.getClass()
        .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class), controller);

    //when
    final Object handle = handlerAdapters.handle(request, response, handlerExecution);

    //then
    assertThat(handle).isInstanceOf(ModelAndView.class);
  }
}
