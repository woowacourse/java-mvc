package support;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode;

public class CustomReflectionUtils {

    private static final int FIRST_INDEX_FOR_UNIQUE_FIELD = 0;

    public static <T> T readFieldValue(Object instance, String fieldName) {
        final List<Field> fields = ReflectionUtils.findFields(
                instance.getClass(),
                field -> field.getName().equalsIgnoreCase(fieldName),
                HierarchyTraversalMode.TOP_DOWN
        );

        final List<Object> fieldValue = ReflectionUtils.readFieldValues(fields, instance, field -> true);

        return (T) fieldValue.get(FIRST_INDEX_FOR_UNIQUE_FIELD);
    }

    private CustomReflectionUtils() {
    }
}
