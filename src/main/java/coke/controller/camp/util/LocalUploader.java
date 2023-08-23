package coke.controller.camp.util;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class LocalUploader {

    @Value("${coke.controller.upload.path}")
    private String uploadPath;

    public List<String> uploadLocal(MultipartFile multipartFile){

        if (multipartFile == null || multipartFile.isEmpty()){
            return null;
        }

        String uuid = UUID.randomUUID().toString();

        String saveFileName = uuid + "_" + multipartFile.getOriginalFilename();

        Path savePath = Paths.get(uploadPath, saveFileName);
        log.info("savePath: " + savePath);

        List<String> savePathList = new ArrayList<>();

        try {
            multipartFile.transferTo(savePath);

            savePathList.add(savePath.toFile().getAbsolutePath());

            if (Files.probeContentType(savePath).startsWith("image")){

                File thumbnailFile = new File(uploadPath, "s_" + saveFileName);
                log.info("thumbnailFIle: " + thumbnailFile);

                savePathList.add(thumbnailFile.getAbsolutePath());
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);

            }
        } catch (Exception e) {

            log.error("ERROR: " + e.getMessage());
            e.printStackTrace();

        }
        return savePathList;
    }

}
