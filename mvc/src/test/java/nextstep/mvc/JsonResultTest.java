package nextstep.mvc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import com.techcourse.air.mvc.core.adapter.AnnotationHandlerAdapter;
import com.techcourse.air.mvc.core.controller.tobe.HandlerExecution;
import com.techcourse.air.mvc.core.returnvalue.JsonReturnValueHandler;
import com.techcourse.air.mvc.core.returnvalue.ViewReturnValueHandler;
import com.techcourse.air.mvc.core.view.JsonView;
import com.techcourse.air.mvc.core.view.ModelAndView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JsonResultTest {

    private AnnotationHandlerAdapter handlerAdapter;
    private TestController testController;
    private Method jsonString;
    private Method jsonObject;
    private Method jsonView;
    private Method jsonView2;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        ApplicationContext context = new ApplicationContext("samples");
        ApplicationContextProvider.setApplicationContext(context);
        context.initializeContext();

        testController = new TestController();
        Class<? extends TestController> clazz = testController.getClass();
        jsonString = clazz.getDeclaredMethod("jsonString", HttpServletRequest.class, HttpServletResponse.class);
        jsonObject = clazz.getDeclaredMethod("jsonObject", HttpServletRequest.class, HttpServletResponse.class);
        jsonView = clazz.getDeclaredMethod("jsonView", HttpServletRequest.class, HttpServletResponse.class);
        jsonView2 = clazz.getDeclaredMethod("jsonView2", HttpServletRequest.class, HttpServletResponse.class);
        context.getBean(ViewReturnValueHandler.class);
        context.getBean(JsonReturnValueHandler.class);
        handlerAdapter = context.findBeanByType(AnnotationHandlerAdapter.class);
    }

    @Test
    @DisplayName("@ResponseBody & String을 리턴하는 컨트롤러의 반환값은 String")
    void string() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        // when
        doNothing().when(response).setContentType(anyString());
        when(response.getWriter()).thenReturn(writer);

        HandlerExecution handlerExecution = new HandlerExecution(testController, jsonString);
        ModelAndView modelAndView = handlerExecution.handle(request, response);
        writer.flush();

        // then
        assertThat(modelAndView).isNull();
        assertThat(stringWriter.toString()).isEqualTo("ok");
    }

    @Test
    @DisplayName("@ResponseBody & Object를 리턴하는 컨트롤러의 반환값은 json")
    void object() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        // when
        doNothing().when(response).setContentType(anyString());
        when(response.getWriter()).thenReturn(writer);

        HandlerExecution handlerExecution = new HandlerExecution(testController, jsonObject);
        ModelAndView modelAndView = handlerExecution.handle(request, response);
        writer.flush();

        // then
        assertThat(modelAndView).isNull();
        String expected = "{\"id\":1,\"account\":\"air\",\"password\":\"1234\",\"email\":\"air.junseo@gmail.com\"}";
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("ModelAndView를 통해 JsonView를 렌더링하는 컨트롤러(모델 1개)의 반환값은 json")
    void modelAndView() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when

        HandlerExecution handlerExecution = new HandlerExecution(testController, jsonView);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
    }

    @Test
    @DisplayName("ModelAndView를 통해 JsonView를 렌더링하는 컨트롤러(모델 2개)의 반환값은 json")
    void modelAndView2() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        HandlerExecution handlerExecution = new HandlerExecution(testController, jsonView2);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
    }
}
