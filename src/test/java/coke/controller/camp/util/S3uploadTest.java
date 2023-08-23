package coke.controller.camp.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class S3uploadTest {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    public void uploadS3(){

        try {

            String filePath = "C:\\sss\\zkzk.jpg";

            String uploadName = s3Uploader.upload(filePath);

            log.info(uploadName);
        } catch (Exception e) {
           log.error(e.getMessage());
        }

    }
}
