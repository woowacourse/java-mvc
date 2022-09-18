package nextstep.mvc.controller.tobe;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller 패키지를 추상화합니다.
 */
public class ControllerPackage {

    private final String name;

    public ControllerPackage(final String name) {
        this.name = name;
    }

    public static List<ControllerPackage> from(final List<String> packageNames) {
        return packageNames.stream()
                .map(ControllerPackage::new)
                .collect(Collectors.toList());
    }

    public List<String> getClassNames() {
        String packagePath = packageNameToPath(name);

        File packageDirectoryFile = getPackageDirectoryFile(packagePath);
        String[] fqcns = packageDirectoryFile.list();

        if (fqcns == null) {
            throw new IllegalArgumentException("클래스를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return Arrays.stream(fqcns)
                .map(it -> it.replace(".class", ""))
                .map(it -> name + "." + it)
                .collect(Collectors.toList());
    }

    private String packageNameToPath(final String packageName) {
        return packageName.replace(".", "/");
    }

    private File getPackageDirectoryFile(final String packageDirectoryPath) {
        URL packageUrl = Thread.currentThread()
                .getContextClassLoader()
                .getResource(packageDirectoryPath);

        if (packageUrl == null) {
            throw new IllegalArgumentException("패키지를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return new File(packageUrl.getFile());
    }
}
