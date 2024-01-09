package org.example;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static ArrayList<File> files;
    private static ArrayList<File> imagesFiles;
    private static ArrayList<File> soundsFiles;
    private static ArrayList<File> textFiles;
    private static ArrayList<File> videosFiles;
    private static ArrayList<File> screenShotsFiles;
    private static List<FileData> filesData;

    public static void main(String[] args){
        // הצילום מאבד קצת מהאיכות

        createDirectoryIfDoesntExist();
        fillDirectoriesListFields();
        optionsMenu();
    }
    private static void optionsMenu(){
        Scanner scanner=new Scanner(System.in);
        final String option1="1";final String option2="2"; final String option3="3"; final String option4="4";
        boolean invalidOption3;
        String decision;
        boolean isFilesDirectoryEmpty=files.isEmpty();
        do {
            System.out.println("press "+option1+" to add a screen shot.");
            System.out.println("press "+option2+" to add a file from your local files.");
            if (!isFilesDirectoryEmpty){
                System.out.println("press "+option3+" for existing files options.");
            }
            decision = scanner.nextLine();
            invalidOption3 = decision.equals(option3) && isFilesDirectoryEmpty;
        } while ((!decision.equals(option1) && !decision.equals(option2) && !decision.equals(option3)) || invalidOption3);
        if (decision.equals(option1)) {
            screenShotsFiles.add(shootAndSaveScreenShot());
        }else if (decision.equals(option2)){
            files.add(promptUserToSelectAFileFromLocalFiles());
        }else if (decision.equals(option3)){
            fileOptionsMenu();
        }
        optionsMenu();
    }
    private static void fileOptionsMenu(){
        Scanner scanner=new Scanner(System.in);
        final String goBackOption="0";
        final String option1="1";final String option2="2"; final String option3="3";
        final String option4="4";final String option5="5"; final String option6="6";
        String decision;
        do {
            System.out.println("press "+goBackOption+" to go back.");
            System.out.println("press "+option1+" to view all files.");
            System.out.println("press "+option2+" to delete a file.");
            System.out.println("press "+option3+" to delete all files.");
            System.out.println("press "+option4+" retrieve text from an image file.");
            System.out.println("press "+option5+" retrieve all colors from an image file.");
            System.out.println("press "+option6+" XXX.");
            decision=scanner.nextLine();
        }while (!decision.equals(goBackOption)&&!decision.equals(option1)&&!decision.equals(option2)&&!decision.equals(option3)&&!decision.equals(option4)&&!decision.equals(option5)&&!decision.equals(option6));
        if (decision.equals(goBackOption)){
            optionsMenu();return;
        } else if (decision.equals(option1)){
            printAllFiles();
        }else if (decision.equals(option2)){
            promptUserToChooseAFileToDelete();
        }else if (decision.equals(option3)){
            deleteAllFiles();
        }else if (decision.equals(option4)){
            retrieveTextFromImageFile();
        }else if (decision.equals(option5)){
            retrieveAllColorsFromImageFile();
        }else if (decision.equals(option6)){

        }
        fileOptionsMenu();
    }
    private static File shootAndSaveScreenShot() {
        //TODO: make a listener for escape to cancel the action.
        //TODO: make an example of this method so to add to useful methods.
        //TODO: fix an issue-> the screen shot files are being written over previous files.
        System.out.println("press the 'SHIFT' button to take a screen shot.");
        System.out.println("press the 'ESCAPE' button to cancel the action.");

        File[] screenShot = {null};
        final boolean[] cancelled = {false};
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF); // מכבה את ההערות כי זה היה מוגזם
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println("There was a problem registering the native hook.");
            return null;
        }
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
                    try {
                        Robot robot = new Robot();
                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                        String fileName = "screenshot" + ((screenShotsFiles == null) ? 1 : screenShotsFiles.size() + 1);
                        File outputFile = new File(Constants.screenShotsDirectoryPath, "/" + fileName + ".png");
                        ImageIO.write(screenFullImage, "png", outputFile);
                        screenShot[0] = outputFile;
                        System.out.println("screen shot was taken.");
                    } catch (AWTException | IOException ex) {
                        System.err.println("Error in capturing screen: " + ex.getMessage());
                    }
                }else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    cancelled[0] = true; // Set the cancelled flag
                    GlobalScreen.removeNativeKeyListener(this);
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ex) {
                        System.err.println("Error in unregistering native hook: " + ex.getMessage());
                    }
                }
            }
            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
                    GlobalScreen.removeNativeKeyListener(this);
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ex) {
                        System.err.println("Error in unregistering native hook: " + ex.getMessage());
                    }
                }
            }
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
                // This method is required, but you don't need to put anything here for this functionality
            }
        });
        while (screenShot[0] == null && !cancelled[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (cancelled[0]) {
            System.out.println("Action cancelled.");
            return null;
        }
        return screenShot[0];
    }
    private static void retrieveAllColorsFromImageFile(){
        Scanner scanner=new Scanner(System.in);
        final String goBackOption="0";final String option1="1";final String option2="2";
        String decision;
        do {
            System.out.println("press " + goBackOption + " to go back.");
            System.out.println("press " + option1 + " to give a file by name.");
            System.out.println("press " + option2 + " to view the list of files.");
            decision=scanner.nextLine();
        }while (!decision.equals(goBackOption)&&!decision.equals(option1)&&!decision.equals(option2));
        if (decision.equals(goBackOption)) {
            fileOptionsMenu();return;
        }else if (decision.equals(option1)){
            boolean isValidName;
            File fileTextToExtract=null;
            do {
                isValidName=false;
                System.out.println("choose the name of the file whom colors you wish to extract:");
                decision=scanner.nextLine();
                for (File file:files){
                    if (file.getName().equals(decision)){
                        isValidName=true;
                        fileTextToExtract=file;
                        break;
                    }
                }
            }while (!isValidName);
            String text=extractTextFromImage(fileTextToExtract.getPath());
            System.out.println(text+"\n");
        }else {
            HashMap<Integer,File> validNumberedFiles=printAllImageFiles();
            int decisionNumber;
            do {
                System.out.println("choose the number of the file whom colors you wish to extract:");
                try {
                    decision = scanner.nextLine();
                    decisionNumber = Integer.parseInt(decision);
                } catch (NumberFormatException e) {
                    decisionNumber = -1;
                }
                if (decision.equals(goBackOption)){
                    fileOptionsMenu();return;}
            } while (decisionNumber < 1 || decisionNumber > validNumberedFiles.size());
            for (Map.Entry<Integer,File> entry:validNumberedFiles.entrySet()){
                if (entry.getKey().equals(decisionNumber)){
                    try {
                        for (Color color:new ColorCollector(entry.getValue()).getUniqueColors()){
                            System.out.println(color.getRGB());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
        retrieveAllColorsFromImageFile();
    }
    private static void retrieveTextFromImageFile(){
        Scanner scanner=new Scanner(System.in);
        final String goBackOption="0";final String option1="1";final String option2="2";
        String decision;
        do {
            System.out.println("press " + goBackOption + " to go back.");
            System.out.println("press " + option1 + " to give a file by name.");
            System.out.println("press " + option2 + " to view the list of files.");
            decision=scanner.nextLine();
        }while (!decision.equals(goBackOption)&&!decision.equals(option1)&&!decision.equals(option2));
        if (decision.equals(goBackOption)) {
            fileOptionsMenu();return;
        }else if (decision.equals(option1)){
            boolean isValidName;
            File fileTextToExtract=null;
            do {
                isValidName=false;
                System.out.println("choose the name of the file whom text you wish to extract:");
                decision=scanner.nextLine();
                for (File file:files){
                    if (file.getName().equals(decision)){
                        isValidName=true;
                        fileTextToExtract=file;
                        break;
                    }
                }
            }while (!isValidName);
            String text=extractTextFromImage(fileTextToExtract.getPath());
            System.out.println(text+"\n");
        }else {
            HashMap<Integer,File> validNumberedFiles= printAllImageFiles();
            int decisionNumber;
            do {
                System.out.println("choose the number of the file whom text you wish to extract:");
                try {
                    decision = scanner.nextLine();
                    decisionNumber = Integer.parseInt(decision);
                } catch (NumberFormatException e) {
                    decisionNumber = -1;
                }
                if (decision.equals(goBackOption)){
                    fileOptionsMenu();return;}
            } while (decisionNumber < 1 || decisionNumber > validNumberedFiles.size());
            for (Map.Entry<Integer,File> entry:validNumberedFiles.entrySet()){
                if (entry.getKey().equals(decisionNumber)){
                    String text=extractTextFromImage(entry.getValue().getPath());
                    System.out.println(text+"\n");
                }
            }
        }
        retrieveTextFromImageFile();
    }
    private static void deleteAllFiles(){for (File file:files){file.delete();}files=new ArrayList<>();}
    private static void promptUserToChooseAFileToDelete(){
        Scanner scanner=new Scanner(System.in);
        final String goBackOption="0";final String option1="1";final String option2="2";
        String decision;
        do {
            System.out.println("press " + goBackOption + " to go back.");
            System.out.println("press " + option1 + " to delete file by name.");
            System.out.println("press " + option2 + " to view the list of files.");
            decision=scanner.nextLine();
        }while (!decision.equals(goBackOption)&&!decision.equals(option1)&&!decision.equals(option2));
        if (decision.equals(goBackOption)){
            fileOptionsMenu();return;
        }else if (decision.equals(option1)){
            boolean isValidName;
            File fileToRemove=null;
            do {
                isValidName=false;
                System.out.println("choose the name of the file you wish to delete:");
                decision=scanner.nextLine();
                for (File file:files){
                    if (file.getName().equals(decision)){
                        isValidName=true;
                        fileToRemove=file;
                        break;
                    }
                }
            }while (!isValidName);
            files.remove(fileToRemove);
            fileToRemove.delete();
            System.out.println("file deleted successfully.\n");
        }else {
            HashMap<Integer,File> numberedFiles=printAllFiles();
            int decisionNumber;
            do {
                System.out.println("choose the number of the file you wish to delete:");
                try {
                    decision = scanner.nextLine();
                    decisionNumber = Integer.parseInt(decision);
                } catch (NumberFormatException e) {
                    decisionNumber = -1;
                }
                if (decision.equals(goBackOption)){
                    fileOptionsMenu();return;}
            } while (decisionNumber < 1 || decisionNumber > numberedFiles.size());
            for (Map.Entry<Integer,File> entry:numberedFiles.entrySet()){
                if (entry.getKey().equals(decisionNumber)){
                    entry.getValue().delete();
                    files.remove(entry.getValue());
                    System.out.println("file deleted successfully.\n");
                }
            }
        }
        promptUserToChooseAFileToDelete();
    }
    private static File promptUserToSelectAFileFromLocalFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setOpacity(0.0f);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        int result = fileChooser.showOpenDialog(frame);
        frame.dispose();
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String directoryPath = "";
            if (Utility.isValidTextFile(selectedFile)){directoryPath=Constants.textDirectoryPath;
            }else if (Utility.isValidVideoFile(selectedFile)){directoryPath=Constants.videosDirectoryPath;
            }else if (Utility.isValidSoundFile(selectedFile)){directoryPath=Constants.soundsDirectoryPath;
            }else if (Utility.isValidImageFile(selectedFile)){directoryPath=Constants.imagesDirectoryPath;}
            File destination = new File(new File(directoryPath), selectedFile.getName());
            copyFileUsingChannel(selectedFile, destination);
            return destination;
        }
        return null;
    }
    private static void copyFileUsingChannel(File source, File dest) {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static HashMap<Integer,File> printAllFiles(){
        HashMap<Integer,File> numberedFiles=new HashMap<>();
        int counter=1;
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(counter+". "+file.getName());
                    numberedFiles.put(counter,file);
                }
                counter++;
            }
        }
        return numberedFiles;
    }
    private static HashMap<Integer,File> printAllImageFiles(){
        HashMap<Integer,File> validNumberedFiles=new HashMap<>();
        int counter=1;
        for (File file:files){
            for (int i=0;i<Constants.validImageFileExtensions.length;i++){
                if (Utility.getFileExtension(file).equals(Constants.validImageFileExtensions[i])){
                    validNumberedFiles.put(counter,file);
                    System.out.println(counter+". "+file.getName());
                    counter++;
                    break;
                }
            }
        }
        return validNumberedFiles;
    }
    private static File editScreenShot(String imagePath) {
        File outputFile = null;
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            BufferedImage enhancedImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            float scaleFactor = 1.2f;
            float offset = 20;
            RescaleOp rescaleOp = new RescaleOp(scaleFactor, offset, null);
            rescaleOp.filter(originalImage, enhancedImage);
            outputFile = new File(Constants.screenShotsDirectoryPath+"/enhancedScreenShot.png");
            ImageIO.write(enhancedImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }
    private static String extractTextFromImage(String imagePath) {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        try {
            return tesseract.doOCR(new File(imagePath));
        } catch (TesseractException e) {
            System.err.println("Error during OCR: " + e.getMessage());
            return "";
        }
    }
    private static ArrayList<File> getAllFilesAsList(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles(); // This will get all files and directories in the folder
        ArrayList<File> filesList = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    filesList.add(file); // Add file to list
                } else if (file.isDirectory()) {
                    filesList.addAll(getAllFilesAsList(file.getAbsolutePath())); // Recursive call for subdirectories
                }
            }
        }
        return filesList;
    }
    private static void createDirectoryIfDoesntExist(){
        File filesDirectory = new File(Constants.filesDirectoryPath);
        List<Boolean> didCreateDirectory=new ArrayList<>();
        if (!filesDirectory.exists()) {
            didCreateDirectory.add(filesDirectory.mkdirs());
            didCreateDirectory.add(new File(Constants.imagesDirectoryPath).mkdirs());
            didCreateDirectory.add(new File(Constants.soundsDirectoryPath).mkdirs());
            didCreateDirectory.add(new File(Constants.textDirectoryPath).mkdirs());
            didCreateDirectory.add(new File(Constants.videosDirectoryPath).mkdirs());
            didCreateDirectory.add(new File(Constants.screenShotsDirectoryPath).mkdirs());
        }
        if (didCreateDirectory.contains(false)){System.out.println("not all directories created successfully!");}
    }
    private static void fillDirectoriesListFields(){
        files=getAllFilesAsList(Constants.filesDirectoryPath);
        if (!Utility.isDirectoryEmpty(Constants.imagesDirectoryPath)){
            imagesFiles.addAll(Arrays.asList(Objects.requireNonNull(new File(Constants.imagesDirectoryPath).listFiles())));
        }else {
            imagesFiles=new ArrayList<>();
        }
        if (!Utility.isDirectoryEmpty(Constants.soundsDirectoryPath)){
            soundsFiles.addAll(Arrays.asList(Objects.requireNonNull(new File(Constants.soundsDirectoryPath).listFiles())));
        }else {
            soundsFiles=new ArrayList<>();
        }
        if (!Utility.isDirectoryEmpty(Constants.textDirectoryPath)){
            textFiles.addAll(Arrays.asList(Objects.requireNonNull(new File(Constants.textDirectoryPath).listFiles())));
        }else {
            textFiles=new ArrayList<>();
        }
        if (!Utility.isDirectoryEmpty(Constants.videosDirectoryPath)){
            videosFiles.addAll(Arrays.asList(Objects.requireNonNull(new File(Constants.videosDirectoryPath).listFiles())));
        }else {
            videosFiles=new ArrayList<>();
        }
        if (!Utility.isDirectoryEmpty(Constants.screenShotsDirectoryPath)){
            screenShotsFiles.addAll(Arrays.asList(Objects.requireNonNull(new File(Constants.screenShotsDirectoryPath).listFiles())));
        }else {
            screenShotsFiles=new ArrayList<>();
        }
    }

}