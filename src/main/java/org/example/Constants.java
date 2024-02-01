package org.example;

import java.util.HashMap;

public class Constants {
    public static final String rootPath="src/main/resources";
    public static final String filesDirectoryName="files";
    public static final String filesDirectoryPath=rootPath+"/"+filesDirectoryName;
    public static final String screenShotsDirectoryName="screenShots";
    public static final String screenShotsDirectoryPath=filesDirectoryPath+"/"+screenShotsDirectoryName;
    public static final String videosDirectoryName="videos";
    public static final String videosDirectoryPath=filesDirectoryPath+"/"+videosDirectoryName;
    public static final String imagesDirectoryName="images";
    public static final String imagesDirectoryPath=filesDirectoryPath+"/"+imagesDirectoryName;
    public static final String textDirectoryName="text";
    public static final String textDirectoryPath=filesDirectoryPath+"/"+textDirectoryName;
    public static final String soundsDirectoryName="sounds";
    public static final String soundsDirectoryPath=filesDirectoryPath+"/"+soundsDirectoryName;
    public static final String[] validImageFileExtensions={"png","jpg","jpeg","gif","bmp","tiff"};
    public static final String[] validVideoFileExtensions = {"mp4", "avi", "mov", "mkv", "flv", "wmv", "m4v", "webm"};
    public static final String[] validSoundFileExtensions = {"mp3", "wav", "aac", "flac", "ogg", "m4a", "wma"};
    public static final String[] validTextFileExtensions = {"txt", "doc", "docx", "pdf", "rtf", "odt", "html", "htm", "tex", "csv", "log", "md"};
    public static final String[] allSubDirectoriesNames={screenShotsDirectoryName,videosDirectoryName,imagesDirectoryName,textDirectoryName,soundsDirectoryName};
    public static final String[] allSubDirectoriesPaths ={screenShotsDirectoryPath,videosDirectoryPath,imagesDirectoryPath,textDirectoryPath,soundsDirectoryPath};

    public static HashMap<String,String> getFilesOptions(){
        final HashMap<String,String> options=new HashMap<>();
        options.put("0","press '0' to go back.");
        options.put("1","press '1' to view all files.");
        options.put("2","press '2' to delete a file.");
        options.put("3","press '3' to delete all files.");
        options.put("4","press '4' to retrieve text from an image file.");
        options.put("5","press '5' to retrieve all colors from an image file.");
        options.put("6","press '6' to rename a file.");
        return options;
    }

}
