package nextstep.mvc.view;

import java.io.File;
import java.util.Arrays;

public abstract class FileViewResolver implements ViewResolver {

    private final String extension;
    private final String pathName;

    protected FileViewResolver(String extension, String pathName) {
        this.extension = extension;
        this.pathName = pathName;
    }

    @Override
    public boolean supports(String viewName) {
        if (viewName.contains(extension)) {
            return true;
        }
        return scanView(viewName);
    }

    private boolean scanView(String viewName) {
        String[] fileNames = scanFiles();
        if (fileNames == null || fileNames.length == 0) {
            return false;
        }
        String viewFileName = ViewUtils.removeRedirect(viewName);
        return Arrays.stream(fileNames)
                .filter(name -> name.endsWith(extension))
                .anyMatch(name -> name.contains(viewFileName));
    }

    private String[] scanFiles() {
        File directory = new File(pathName);
        return directory.list();
    }
}
