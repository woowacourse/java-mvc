package nextstep.mvc.support;

public class FileNameHandlerUtils {

    private FileNameHandlerUtils() {
    }

    public static String getExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex < 0 || extensionIndex >= fileName.length() - 1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static boolean isExtension(String fileName, String extension){
        return extension.equals(getExtension(fileName));
    }
}
