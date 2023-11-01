package coke.controller.camp.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // s3로 파일 업로드하기
    public String upload(String filePath)throws RuntimeException{

        log.info("---------------s3 upload----------------");
        log.info("filePath: " + filePath);

        File targetFile = new File(filePath);

        log.info("targetFile: " + targetFile);

        String uploadImageUrl = putS3(targetFile, targetFile.getName()); // s3로 업로드

        removeOriginalFile(targetFile);
        log.info("uploadImageUrl: " + uploadImageUrl);

        return uploadImageUrl;
    }

    // s3로 업로드
    private String putS3(File uploadFile, String fileName)throws RuntimeException{


        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();

    }

    // s3 업로드 후 원본 파일 삭제
    private void removeOriginalFile(File targetFile){
        if (targetFile.exists() && targetFile.delete()){
            log.info("File delete success");
            return;
        }
        log.info("fail to remove files");
    }

    public void removeS3File(String fileName){

        log.info("-----------removeS3File------------");
        log.info(fileName);

        String thumbnail = "s_" + fileName;
        log.info(thumbnail);

        final List<DeleteObjectRequest> deleteObjectRequestList = new ArrayList<>();

       deleteObjectRequestList.add(new DeleteObjectRequest(bucket, fileName));
       deleteObjectRequestList.add(new DeleteObjectRequest(bucket, thumbnail));

       deleteObjectRequestList.forEach(deleteObjectRequest -> {
           amazonS3Client.deleteObject(deleteObjectRequest);
       });

    }

}
