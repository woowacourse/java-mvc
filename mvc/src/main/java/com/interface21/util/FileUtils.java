package com.interface21.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

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
}
