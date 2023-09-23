package webmvc.org.springframework.web.servlet.mvc.supports.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.View;
import webmvc.org.springframework.web.servlet.mvc.view.JsonView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewResolverTest {

    @Test
    void 생성자는_호출하면_JsonViewResolver를_초기화한다() {
        assertDoesNotThrow(JsonViewResolverTest::new);
    }

    @Test
    void supports_메서드는_요청_헤더_Accept에_json이_있다면_true를_반환한다() {
        final JsonViewResolver viewResolver = new JsonViewResolver();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Accept")).willReturn("application/json");

        final boolean actual = viewResolver.supports(request, null);

        assertThat(actual).isTrue();
    }

    @Test
    void supports_메서드는_요청_헤더_Accept에_json이_없다면_false를_반환한다() {
        final JsonViewResolver viewResolver = new JsonViewResolver();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Accept")).willReturn(null);

        final boolean actual = viewResolver.supports(request, null);

        assertThat(actual).isFalse();
    }

    @Test
    void resolve_메서드는_View를_초기화해_반환한다() {
        final JsonViewResolver viewResolver = new JsonViewResolver();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Accept")).willReturn("application/json");

        final View actual = viewResolver.resolve(null);

        assertThat(actual).isExactlyInstanceOf(JsonView.class);
    }
}
