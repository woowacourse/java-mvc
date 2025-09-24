package com.interface21.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.context.stereotype.Controller;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReflectionUtilsTest {

    @Nested
    class getTypesAnnotatedWith {

        @Test
        void 성공() {
            // given
            final String[] packageNames = {"samples"};
            final Class annotation = Controller.class;

            // when
            final Set<Class<?>> result = ReflectionUtils.getTypesAnnotatedWith(packageNames, annotation);

            // then
            assertThat(result).hasSize(1);
        }
    }
}
