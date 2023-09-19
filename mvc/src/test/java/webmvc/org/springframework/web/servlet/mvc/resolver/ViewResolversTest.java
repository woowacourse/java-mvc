package webmvc.org.springframework.web.servlet.mvc.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.view.resolver.ViewResolvers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ViewResolversTest {

    @Test
    void 생성자는_호출하면_ViewResolvers를_초기화한다() {
        assertDoesNotThrow(ViewResolvers::new);
    }

    @Test
    void initialize_메서드는_호출하면_필요한_viewResolvers를_등록한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThatCode(viewResolvers::initialize).doesNotThrowAnyException();
            softAssertions.assertThat(viewResolvers.findView(request, "/hello.jsp")).isNotNull();
        });
    }

    @Test
    void isEmpty_메서드는_viewResolver에_등록된_resolver가_없다면_true를_반환한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();

        final boolean actual = viewResolvers.isEmpty();

        assertThat(actual).isTrue();
    }

    @Test
    void isEmpty_메서드는_viewResolver에_등록된_resolver가_있다면_false를_반환한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.initialize();

        final boolean actual = viewResolvers.isEmpty();

        assertThat(actual).isFalse();
    }

    @Test
    void findView_메서드는_해당_요청과_경로를_분석할_수_있다면_View를_반환한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final View actual = viewResolvers.findView(request, "/hello.jsp");

        assertThat(actual).isNotNull();
    }

    @Test
    void findView_메서드는_해당_요청과_경로를_분석할_수_없다면_View를_반환한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final View actual = viewResolvers.findView(request, "/hello.jsp");

        assertThat(actual).isNull();
    }
}
