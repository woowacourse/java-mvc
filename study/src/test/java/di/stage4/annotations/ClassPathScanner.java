package di.stage4.annotations;

import static org.reflections.scanners.Scanners.SubTypes;

import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        // Object.class의 모든 하위 타입의 클래스를 스캔하기 위해 SubTypes Scanner를 추가
        return new Reflections(packageName, SubTypes.filterResultsBy(c -> true)).getSubTypesOf(Object.class);
    }
}
