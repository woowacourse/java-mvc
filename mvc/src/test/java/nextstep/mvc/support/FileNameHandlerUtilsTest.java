package nextstep.mvc.support;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileNameHandlerUtilsTest {

    @DisplayName("파일 확장자를 추출한다.")
    @Test
    void extension() {
        String fileName = "hi.jsp";
        String extension = FileNameHandlerUtils.getExtension(fileName);
        Assertions.assertThat(extension).isEqualTo("jsp");
    }

    @DisplayName("파일 확장자가 없는 경우 null을 반환한다.")
    @Test
    void withOutExtension() {
        String fileName1 = "hi";
        String extension1 = FileNameHandlerUtils.getExtension(fileName1);
        Assertions.assertThat(extension1).isNull();

        String fileName2 = "hi.";
        String extension2 = FileNameHandlerUtils.getExtension(fileName2);
        Assertions.assertThat(extension2).isNull();
    }
}