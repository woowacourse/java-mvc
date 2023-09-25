package di.stage4.annotations;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) throws Exception {
        final Set<Class<?>> classes = new HashSet<>();
        final Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(packageName.replace(".","/"));

        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                final File packageDir = new File(resource.getFile());
                findClassesInDirectory(packageName, packageDir, classes);
            }
        }
        return classes;
    }

    private static void findClassesInDirectory(String packageName, File directory, Set<Class<?>> classes) throws ClassNotFoundException {
        final File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (final File file : files) {
            if (file.isDirectory()) {
                findClassesInDirectory(packageName + "." + file.getName(), file, classes);
            } else if (file.getName().endsWith(".class")) {
                final String className = packageName + "." + file.getName().replace(".class", "");
                final Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Service.class) ||
                        clazz.isAnnotationPresent(Repository.class) ||
                        clazz.isAnnotationPresent(Inject.class)) {
                    classes.add(clazz);
                }
            }
        }
    }
}
