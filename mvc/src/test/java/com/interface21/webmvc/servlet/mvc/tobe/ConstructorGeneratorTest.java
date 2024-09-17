package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.PrivateCrew;
import samples.PublicCrew;

class ConstructorGeneratorTest {

    @Test
    @DisplayName("기본 생성자로 객체를 생성한다.")
    void generate() {
        final Object crew = ConstructorGenerator.generate(PublicCrew.class);
        assertThat(crew.getClass()).isEqualTo(PublicCrew.class);
    }

    @Test
    @DisplayName("생성자가 private이여도 generate 메서드는 접근제어자 변경후 객체를 생성한다.")
    void fail_generate() throws Exception {
        final Class<PrivateCrew> privateCrewClass = PrivateCrew.class;
        final Constructor<PrivateCrew> declaredConstructor = privateCrewClass.getDeclaredConstructor();
        assertThatThrownBy(declaredConstructor::newInstance)
                .isInstanceOf(IllegalAccessException.class);
        assertThatCode(() -> ConstructorGenerator.generate(PrivateCrew.class))
                .doesNotThrowAnyException();
    }
}
