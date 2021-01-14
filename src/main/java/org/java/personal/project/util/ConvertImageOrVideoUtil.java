package org.java.personal.project.util;

import org.java.personal.project.constant.MediaFormatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.java.personal.project.constant.AppEnum.*;

@Service
public class ConvertImageOrVideoUtil {

    private final Environment env;

    @Autowired
    public ConvertImageOrVideoUtil(Environment env) {
        this.env = env;
    }

    public List<String> convertFileToBase64String(List<String> postPictures, List<String> postBases64) throws IOException {

        for(String postPicture : postPictures){
            String postFileInString = convertFileOneByOne(postPicture);
            postBases64.add(postFileInString);
        }
        return postBases64;
    }

    public String convertFileOneByOne(String postPicture) throws IOException {
        File currentPostFile = new File(env.getProperty("postPicturePath") + postPicture);
        if(currentPostFile == null)
            return PICTURE_CANNOT_LOAD_PROPERLY.getMessage();

        byte[] postFileByte = Files.readAllBytes(currentPostFile.toPath().toAbsolutePath());
        String postFileInString = Base64.getEncoder().encodeToString(postFileByte);

        return postFileInString;
    }

    public List<String> convertImage(byte[] data, MultipartFile file, List<String> postCollections) throws Exception {
        ValidationUtil validationUtil = new ValidationUtil();
        File postFile = new File(env.getProperty("postPicturePath") + file.getOriginalFilename());
        String substringPost = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

        if(!validationUtil.validateFileFormat(substringPost))
            throw new IOException(INVALID_POST_FORMAT.getMessage());


        InputStream inputStream = file.getInputStream();

        int read = 0;
        if(!postFile.exists()) postFile.createNewFile();
        OutputStream outputStream = new FileOutputStream(postFile);
        while((read = inputStream.read(data)) != -1){
            outputStream.write(data, 0, read);
        }

        if(validationUtil.isVideo(substringPost))
            convertVideoToMp4(file, postFile);

        file.transferTo(postFile);
        postFile.getAbsolutePath();

        postCollections.add(file.getOriginalFilename());

        return postCollections;
    }

    private void convertVideoToMp4(MultipartFile file, File postFile) throws Exception {
        File targetFile = new File(env.getProperty("postPicturePath") +
                file.getOriginalFilename().replace(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")), MediaFormatEnum.MP4.getMessage()));

        AudioAttributes audio = new AudioAttributes();
        audio.setBitRate(64000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(400,300));

        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setFormat("mp4");
        attributes.setAudioAttributes(audio);
        attributes.setVideoAttributes(video);

        try{
            ws.schild.jave.Encoder encoder = new Encoder();
            encoder.encode((List<MultimediaObject>) postFile, targetFile, attributes);
        }catch(Exception e){
            throw new Exception(CAN_NOT_ENCODE_THE_VIDEO.getMessage(),e);
        }

    }


}
