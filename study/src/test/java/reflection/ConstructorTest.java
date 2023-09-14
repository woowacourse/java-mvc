package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConstructorTest {

    @Test
    void getConstructorV1() throws Exception {
        // given
        final Constructor<One> constructor = One.class.getConstructor();

        // when
        final One one = constructor.newInstance();

        // then
        assertThat(one.name).isNull();
    }

    @Test
    void getConstructorV2() throws Exception {
        final Constructor<One> constructor = One.class.getConstructor(String.class);
        // given

        // when
        final One one = constructor.newInstance("hyena");

        // then
        assertThat(one.name).isEqualTo("hyena");
    }

    @Test
    void declaredConstructorV1() throws Exception {
        // given
        final Constructor<?> declaredConstructor = One.class.getDeclaredConstructor();

        // when
        final One one = (One) declaredConstructor.newInstance();

        // then
        assertThat(one.name).isNull();
    }

    @Test
    void declaredConstructorV2() {
        assertThatThrownBy(() -> One.class.getDeclaredConstructor(String.class, String.class))
                .isInstanceOf(NoSuchMethodException.class);
    }

    private static class One {

        private String name;

        public One() {
        }

        public One(final String name) {
            this.name = name;
        }

        public One(final String name, final String temp) {

        }

        @Override
        public String toString() {
            return "One{" +
                   "name='" + name + '\'' +
                   '}';
        }
    }
}
