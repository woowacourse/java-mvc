package nextstep.mvc.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {

    @Test
    void 클래스_정보를_가지고_새로운_인스턴스를_생성한다() throws Exception {
        Object actual = ReflectionUtils.createNewInstance(ReflectionUtilsTestClass.class);
        assertThat(actual).isInstanceOf(ReflectionUtilsTestClass.class);
    }

    private static class ReflectionUtilsTestClass {
        public ReflectionUtilsTestClass() {
        }
    }
}
