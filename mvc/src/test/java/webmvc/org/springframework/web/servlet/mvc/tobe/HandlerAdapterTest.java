package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

class HandlerAdapterTest {

    @Test
    void 조건을_만들어_확인할_수_있다() {
        // given
        final HandlerAdapter handlerAdapter = new HandlerAdapter() {
            @Override
            public boolean support(final Object handleExecution) {
                return handleExecution instanceof String;
            }

            @Override
            public ModelAndView doInternalService(final HttpServletRequest request, final HttpServletResponse response, final Object method) throws Exception {
                throw new UnsupportedOperationException("NO TEST");
            }
        };

        // expect
        assertAll(
                () ->assertThat(handlerAdapter.support("문자열")).isTrue(),
                () ->assertThat(handlerAdapter.support(1)).isFalse(),
                () ->assertThat(handlerAdapter.support(new Object())).isFalse()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"안녕하세요.", "헬로우", "에브리바디", "a", "1", "!"})
    void 어떤_기능을_수행할_수_있는_추상화된_메서드가_있다(final String value) throws Exception {
        // given
        final ModelAndView expected = mock(ModelAndView.class);
        final HandlerAdapter handlerAdapter = new HandlerAdapter() {
            @Override
            public boolean support(final Object handleExecution) {
                return handleExecution instanceof String;
            }

            @Override
            public ModelAndView doInternalService(final HttpServletRequest request, final HttpServletResponse response, final Object method) throws Exception {
                return expected;
            }
        };

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // expect
        final Object actual = handlerAdapter.doInternalService(request, response, value);

        assertThat(actual).isEqualTo("로직이 수행된 문자열");
    }
}
