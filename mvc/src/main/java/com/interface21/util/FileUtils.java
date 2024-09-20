package com.interface21.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

public class FileUtils {

    private FileUtils() {
    }

    public static File getPackageDirectory(URL directoryURL) throws FileNotFoundException {
        File packageDirectory = new File(directoryURL.getFile());
        if (!packageDirectory.exists()) {
            throw new FileNotFoundException("디렉토리를 가져오는데 실패하였습니다");
        }
        return packageDirectory;
    }

    public static URL getPackageURL(String packageName) throws FileNotFoundException {
        String packagePath = packageName.replace(".", "/");
        URL packageURL = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        if (packageURL == null) {
            throw new FileNotFoundException("패키지 URL을 가져오는데 실패하였습니다");
        }
        return packageURL;
    }

    public static void processDirectory(
            File directory,
            String packageName,
            List<Class<?>> classes) throws FileNotFoundException, ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new FileNotFoundException("패키지 디렉토리를 나열할 수 없습니다");
        }
        for (File file : files) {
            if (file.isDirectory()) {
                String temp = packageName.isBlank() ? file.getName() : packageName + "." + file.getName();
                processDirectory(file, temp, classes);
                continue;
            }

            if (file.isFile()) {
                getClassFromFile(packageName, classes, file);
            }
        }
    }

    private static void getClassFromFile(String packageName, List<Class<?>> classes, File file)
            throws ClassNotFoundException {
        String fileName = file.getName();
        if (!fileName.endsWith(".class")) {
            return;
        }

        String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
        Class<?> clazz = Class.forName(className);
        classes.add(clazz);
    }

}
