package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import nextstep.web.annotation.RequestParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParameterResolverExecutorTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @DisplayName("알맞은 파라미터를 바인딩하는지 확인")
    @Test
    void captureProperParameter() throws NoSuchMethodException {
        //given
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getAttribute("id")).thenReturn("wedge");

        ParameterResolverExecutor parameterResolverExecutor = new ParameterResolverExecutor(
            Collections.emptyList());
        TestController controller = new TestController();

        Map<String, Class<?>[]> testCases = new LinkedHashMap<>();
        testCases.put("withRequestAndResponse",
            new Class[]{HttpServletRequest.class, HttpServletResponse.class});
        testCases.put("withRequest", new Class[]{HttpServletRequest.class});
        testCases.put("withResponse", new Class[]{HttpServletResponse.class});
        testCases.put("withSession", new Class[]{HttpSession.class});
        testCases.put("withRequestParam", new Class[]{String.class, HttpServletRequest.class});
        //when
        //then
        for (String methodName : testCases.keySet()) {
            Class<?>[] parametersClasses = testCases.get(methodName);
            Method declaredMethods = controller.getClass()
                .getDeclaredMethod(methodName, testCases.get(methodName));
            Object[] parameters = parameterResolverExecutor.captureProperParameter(declaredMethods, request, response);

            assertThat(parameters).hasSize(parametersClasses.length);
            assertParameterTypes(parametersClasses, parameters);
        }
    }

    private void assertParameterTypes(Class<?>[] parametersClasses, Object[] parameters) {
        for (int i = 0; i < parametersClasses.length; i++) {
            assertThat(parameters[i]).isInstanceOf(parametersClasses[i]);
        }
    }

    private static class TestController {

        private void withRequestAndResponse(HttpServletRequest request,
                                            HttpServletResponse response) {
        }

        private void withRequest(HttpServletRequest request) {
        }

        private void withResponse(HttpServletResponse response) {
        }

        private void withSession(HttpSession session) {
        }

        private void withRequestParam(@RequestParam("id") String id, HttpServletRequest request) {
        }
    }
}