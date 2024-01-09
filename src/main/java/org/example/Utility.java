package org.example;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.util.Objects;
import java.util.Random;
public class Utility {
    public static ChromeDriver getModifiedChromeDriver() {
        System.setProperty("webdriver.openqa.driver", "path/to/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en");
        //options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--enable-automation");
        options.addArguments("user-agent=" + Utility.getRandomUserAgent());
        return new ChromeDriver(options);
    }
    public static String getRandomUserAgent(){
        String[] userAgents = new String[10];
        userAgents[0] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";
        userAgents[1] = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15; rv:86.0) Gecko/20100101 Firefox/86.0";
        userAgents[2] = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1";
        userAgents[3] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36 Edg/88.0.705.74";
        userAgents[4] = "Mozilla/5.0 (Linux; Android 10; SM-A505FN) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.152 Mobile Safari/537.36";
        userAgents[5] = "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148";
        userAgents[6] = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:86.0) Gecko/20100101 Firefox/86.0";
        userAgents[7] = "Mozilla/5.0 (Windows NT 10.0; Trident/7.0; rv:11.0) like Gecko";
        userAgents[8] = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36";
        userAgents[9] = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko";
        return userAgents[new Random().nextInt(userAgents.length)];
    }
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



}
