package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewResolverTest {

    private JsonViewResolver jsonViewResolver;

    @BeforeEach
    void setUp() {
        jsonViewResolver = new JsonViewResolver();
    }

    @DisplayName("Json 관련 뷰인지 확인한다.")
    @Test
    void supports() {
        boolean isSupport = jsonViewResolver.supports(null);
        boolean isNotSupport = jsonViewResolver.supports("json_view_아님");

        assertThat(isSupport).isTrue();
        assertThat(isNotSupport).isFalse();
    }

    @DisplayName("뷰 이름으로 뷰 객체를 반환한다.")
    @Test
    void resolveViewName() {
        View view = jsonViewResolver.resolveViewName(null);

        assertThat(view).isInstanceOf(JsonView.class);
    }
}