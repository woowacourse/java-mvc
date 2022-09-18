package nextstep.util;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PackageUtil {

    private PackageUtil() {
    }

    /**
     * 패키지 이름을 전달하여 패키지 내부의 모든 클래스의 FQCN 목록을 반환합니다.
     *
     * @param packageName 패키지 이름
     * @return 패키지에 존재하는 클래스들의 FQCN 목록
     */
    public static List<String> getClassNamesInPackage(final String packageName) {
        String packagePath = packageNameToPath(packageName);

        File packageDirectoryFile = getPackageDirectoryFile(packagePath);
        String[] fqcns = packageDirectoryFile.list();

        if (fqcns == null) {
            throw new IllegalArgumentException("클래스를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return Arrays.stream(fqcns)
                .map(it -> it.replace(".class", ""))
                .map(it -> packageName + "." + it)
                .collect(Collectors.toList());
    }

    private static String packageNameToPath(final String packageName) {
        return packageName.replace(".", "/");
    }

    private static File getPackageDirectoryFile(final String packageDirectoryPath) {
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
