package org.example;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.List;

public class Main {
    private static ArrayList<File> files;
    public static ArrayList<File> imagesFiles;
    public static ArrayList<File> soundsFiles;
    public static ArrayList<File> textFiles;
    public static ArrayList<File> videosFiles;
    public static ArrayList<File> screenShotsFiles;
    public static List<FileData> filesData;

    public static void main(String[] args){
        // הצילום מאבד קצת מהאיכות

        createDirectoryIfDoesntExist();
        fillDirectoriesListFields();
        optionsMenu();
    }
    private static void optionsMenu(){
        Scanner scanner=new Scanner(System.in);
        final String option1="1";final String option2="2"; final String option3="3";
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
            new GlobalScreen().shootAndSaveScreenShot();
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
            System.out.println("press "+option4+" to retrieve text from an image file.");
            System.out.println("press "+option5+" to retrieve all colors from an image file.");
            System.out.println("press "+option6+" to rename a file.");
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
        }else if (decision.equals(option4)||decision.equals(option5)){ // אם משנים את המספר האופציות צריך לשנות גם ב-imageFileRetriever
            imageFileExtractor(decision);
        }else if (decision.equals(option6)){
            promptUserToRenameFile();
        }
        fileOptionsMenu();
    }
    private static void renameFile(Scanner scanner, File file){
        int counter=0;
        String name;
        do {
            if (counter>0){
                System.out.println("file name must be between 5 to 30 characters!");
            }
            System.out.println("insert the name you want:");
            name=scanner.nextLine();
            counter++;
        }while (name.length()<=30 && name.length()>=5);

        file.renameTo(new File(Utility.getFileDirectoryPath(file) + "/" + name + Utility.getFileExtension(file)));
    }
    private static void promptUserToRenameFile(){
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
        }else if (decision.equals(option1)) {
            boolean isValidName;
            File fileToExtract = null;
            do {
                isValidName = false;
                System.out.println("insert the name of the file:");
                decision = scanner.nextLine();
                for (File file : files) {
                    if (file.getName().equals(decision)) {
                        isValidName = true;
                        fileToExtract = file;
                        break;
                    }
                }
            } while (!isValidName);
            renameFile(scanner,fileToExtract);
        }else {
            HashMap<Integer,File> numberedFiles=printAllFiles();
            int decisionNumber;
            do {
                System.out.println("choose a file by number:");
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
                    int counter=0;
                    String name;
                    do {
                        if (counter>0){
                            System.out.println("file name must be between 5 to 30 characters!");
                        }
                        System.out.println("insert the name you want:");
                        name=scanner.nextLine();
                        counter++;
                    }while (name.length()<=30 && name.length()>=5);
                    entry.getValue().renameTo(new File(Utility.getFileDirectoryPath(entry.getValue()) + "/" + name + Utility.getFileExtension(entry.getValue())));
                    break;
                }
            }
        }
        promptUserToRenameFile();
    }

    private static void imageFileExtractor(String chosenOption){
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
            File fileToExtract=null;
            do {
                isValidName=false;
                System.out.println("insert the name of the file:");
                decision=scanner.nextLine();
                for (File file:files){
                    if (file.getName().equals(decision)){
                        if (Utility.isValidImageFile(file)){
                            isValidName=true;
                            fileToExtract=file;
                            break;
                        }else {
                            System.out.println("invalid file type! needs image type!");
                            imageFileExtractor(chosenOption);
                            return;
                        }
                    }
                }
            }while (!isValidName);
            if (chosenOption.equals("4")){
                String text=extractTextFromImage(fileToExtract.getPath());
                System.out.println(text+"\n");
            }else {
                try {
                    for (Color color:new ColorCollector(fileToExtract).getUniqueColors()){
                        System.out.println(color.getRGB());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            HashMap<Integer,File> numberedImageFiles=printAllImageFiles();
            int decisionNumber;
            do {
                System.out.println("choose a file by number:");
                try {
                    decision = scanner.nextLine();
                    decisionNumber = Integer.parseInt(decision);
                } catch (NumberFormatException e) {
                    decisionNumber = -1;
                }
                if (decision.equals(goBackOption)){
                    fileOptionsMenu();return;}
            } while (decisionNumber < 1 || decisionNumber > numberedImageFiles.size());
            for (Map.Entry<Integer,File> entry:numberedImageFiles.entrySet()){
                if (entry.getKey().equals(decisionNumber)){
                    if (chosenOption.equals("4")){
                        String text=extractTextFromImage(entry.getValue().getPath());
                        System.out.println(text+"\n");
                    }else {
                        try {
                            for (Color color:new ColorCollector(entry.getValue()).getUniqueColors()){
                                System.out.println(color.getRGB());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
        imageFileExtractor(chosenOption);
    }
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
        imagesFiles = getFilesFromDirectory(Constants.imagesDirectoryPath);
        soundsFiles = getFilesFromDirectory(Constants.soundsDirectoryPath);
        textFiles = getFilesFromDirectory(Constants.textDirectoryPath);
        videosFiles = getFilesFromDirectory(Constants.videosDirectoryPath);
        screenShotsFiles = getFilesFromDirectory(Constants.screenShotsDirectoryPath);
    }
    private static ArrayList<File> getAllFilesAsList(String path) {
        File[] listOfFiles = new File(path).listFiles();
        ArrayList<File> filesList = new ArrayList<>();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    filesList.add(file);
                } else if (file.isDirectory()) {
                    filesList.addAll(getAllFilesAsList(file.getAbsolutePath()));
                }
            }
        }
        return filesList;
    }
    private static ArrayList<File> getFilesFromDirectory(String directoryPath) {
        File[] filesArray = new File(directoryPath).listFiles();
        if (filesArray != null && filesArray.length > 0) {
            return new ArrayList<>(Arrays.asList(filesArray));
        } else {
            return new ArrayList<>();
        }
    }
    private static void deleteAllFiles(){for (File file:files){file.delete();}files=new ArrayList<>();}


}