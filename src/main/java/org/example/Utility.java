package org.example;
import java.io.File;
public class Utility {
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        } else {
            return "";
        }
    }
    public static boolean isValidImageFile(File file){
        String extension=getFileExtension(file);
        for (int i=0;i<Constants.validImageFileExtensions.length;i++){
            if (extension.equals(Constants.validImageFileExtensions[i])){
                return true;
            }
        }
        return false;
    }
    public static boolean isValidTextFile(File file) {
        String extension=getFileExtension(file);
        for (int i=0;i<Constants.validTextFileExtensions.length;i++){
            if (extension.equals(Constants.validTextFileExtensions[i])){
                return true;
            }
        }
        return false;
    }
    public static boolean isValidSoundFile(File file) {
        String extension=getFileExtension(file);
        for (int i=0;i<Constants.validSoundFileExtensions.length;i++){
            if (extension.equals(Constants.validSoundFileExtensions[i])){
                return true;
            }
        }
        return false;
    }
    public static boolean isValidVideoFile(File file) {
        String extension=getFileExtension(file);
        for (int i=0;i<Constants.validVideoFileExtensions.length;i++){
            if (extension.equals(Constants.validVideoFileExtensions[i])){
                return true;
            }
        }
        return false;
    }
    public static boolean isDirectoryEmpty(String path) {
         return new File(path).list() != null;
    }
    public static String getFileDirectoryPath(File file){
        String path=file.getPath();
        for (int i=0;i<Constants.allSubDirectoriesNames.length;i++){
            if (path.contains(Constants.allSubDirectoriesNames[i])){
                return Constants.allSubDirectoriesPaths[i];
            }
        }
        return null;
    }


}
