package org.example;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;

public class FileDataExtractor {
    private final File file;
    private final String fileExtension;
    private final String fileType;

    public FileDataExtractor(File file) throws IllegalArgumentException {
        if (file == null || !file.isFile()) {
            throw new IllegalArgumentException("Invalid file provided");
        }
        this.file = file;
        this.fileExtension = Utility.getFileExtension(file);
        if (!fileExtension.isEmpty()) {
            this.fileType = getFileType();
        } else {
            throw new IllegalArgumentException("File extension is empty or not recognized");
        }
    }
    public String fileDataExtractorOperator(){
        if (file==null){return null;}

        if (fileType.equals("image")){
            return extractMetaDataFromImageFile();
        }else if (fileType.equals("video")){

        }else if (fileType.equals("sound")){

        }else {

        }

        return null;
    }
    public String getFileType(){
        String fileType=null;
        if (Utility.isValidImageFile(file)){fileType="image";
        }else if (Utility.isValidVideoFile(file)){fileType="video";
        }else if (Utility.isValidSoundFile(file)){fileType="sound";
        }else if (Utility.isValidTextFile(file)){fileType="text";}
        return fileType;
    }

    private String extractMetaDataFromImageFile() {
        StringBuilder metaData = new StringBuilder();
        try {
            Metadata metadata;
            try {
                metadata = ImageMetadataReader.readMetadata(file);
            } catch (ImageProcessingException e) {
                throw new RuntimeException(e);
            }
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    metaData.append(tag).append("\n");
                }

                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        metaData.append("ERROR: ").append(error).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading metadata: " + e.getMessage();
        }

        return metaData.toString();
    }
}
