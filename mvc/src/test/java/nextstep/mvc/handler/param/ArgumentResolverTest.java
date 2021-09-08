package nextstep.mvc.handler.param;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestParam;
import nextstep.web.annotation.SessionAttribute;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentResolverTest {

    private final Object controller = Mockito.mock(Object.class);
    private final HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

    @DisplayName("RequestParam 어노테이션에 맞춰 인자를 반환한다.")
    @Test
    void getArguments() {
        Method testMethod = getTestMethod("requestParam");
        Mockito.when(mockRequest.getParameter("id")).thenReturn("id");

        Object[] arguments = ArgumentResolver.resolveRequestParam(controller, testMethod, mockRequest, mockResponse);
        assertThat(arguments).isEqualTo(new Object[]{"id"});
    }

    @DisplayName("지정한 이름의 key가 요청 파라미터에 없다면 null을 반환한다.")
    @Test
    void nonExistentValue() {
        Method testMethod = getTestMethod("requestParam");
        Mockito.when(mockRequest.getParameter("id")).thenReturn(null);

        Object[] arguments = ArgumentResolver.resolveRequestParam(controller, testMethod, mockRequest, mockResponse);
        assertThat(arguments).isEqualTo(new Object[]{null});
    }

    @DisplayName("SessionAttribute 어노테이션으로 HttpSession을 resolve한다.")
    @Test
    void httpSession() {
        Method testMethod = getTestMethod("session");

        ArgumentResolver.resolveRequestParam(controller, testMethod, mockRequest, mockResponse);
        Mockito.verify(mockRequest).getSession();
    }

    @DisplayName("파라미터 타입이 HttpServletRequest, HttpServletResponse일 경우를 Resolve한다.")
    @Test
    void httpHttpServletRequest() {
        Method testMethod = getTestMethod("httpServletRequest");

        Object[] objects = ArgumentResolver.resolveRequestParam(controller, testMethod, mockRequest, mockResponse);
        assertThat(objects).isEqualTo(new Object[]{mockRequest});
    }

    private Method getTestMethod(String notRequestParam) {
        Class<?> testController = TestController.class;
        return Arrays.stream(testController.getMethods())
                .filter(method -> method.getName().equals(notRequestParam))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("테스트할 메서드가 없습니다."));
    }
}

class TestController {
    @RequestMapping(value = "/request-param", method = RequestMethod.POST)
    public void requestParam(@RequestParam("id") String id) {
    }

    @RequestMapping(value = "/request-param-session", method = RequestMethod.POST)
    public void session(@SessionAttribute jakarta.servlet.http.HttpSession session) {
    }

    @RequestMapping(value = "/request-param-http-servlet", method = RequestMethod.POST)
    public void httpServletRequest(HttpServletRequest request) {
    }
}
