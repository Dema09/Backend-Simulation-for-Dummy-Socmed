package org.java.personal.project.util;

import org.java.personal.project.constant.MediaFormatEnum;

public class ValidationUtil {

    public boolean validateFileFormat(String currentFileFormat){
        if(!currentFileFormat.equalsIgnoreCase(MediaFormatEnum.JPEG.getMessage()) &&
                !currentFileFormat.equalsIgnoreCase(MediaFormatEnum.JPG.getMessage()) &&
                !currentFileFormat.equalsIgnoreCase(MediaFormatEnum.PNG.getMessage()) &&
                !currentFileFormat.equalsIgnoreCase(MediaFormatEnum.MP4.getMessage()) &&
                !currentFileFormat.equalsIgnoreCase(MediaFormatEnum.AVI.getMessage()) &&
                !currentFileFormat.equalsIgnoreCase(MediaFormatEnum.MOV.getMessage())
        )
            return false;
        return true;
    }

    public boolean isVideo(String currentFileFormat){
        if(currentFileFormat.equalsIgnoreCase(MediaFormatEnum.MP4.getMessage()) &&
                currentFileFormat.equalsIgnoreCase(MediaFormatEnum.AVI.getMessage()) &&
                currentFileFormat.equalsIgnoreCase(MediaFormatEnum.MOV.getMessage()))
            return true;
        return false;
    }

}
