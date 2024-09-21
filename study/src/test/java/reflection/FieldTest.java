package reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FieldTest {
    @Test
    @DisplayName("public 멤버 변수만 가져온다.")
    void get_fields_with_public_access() {
        final var fields = Arrays.stream(Room.class.getFields())
                .map(Field::getName)
                .toArray();

        assertThat(fields.length).isOne();
        assertThat(fields).contains("name");
    }

    @Test
    @DisplayName("public,protected,private 접근자에 해당하는 멤버 변수를 전부 가져온다.")
    void get_declare_fields_with_public_protected_private_access() {
        final var fields = Arrays.stream(Room.class.getDeclaredFields())
                .map(Field::getName)
                .toArray();

        assertThat(fields).hasSize(3);
        Assertions.assertAll(() -> {
            assertThat(fields).contains("name");
            assertThat(fields).contains("address");
            assertThat(fields).contains("roomId");
        });
    }

}
