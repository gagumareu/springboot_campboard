package coke.controller.camp.controller;

import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.dto.UploadResultDTO;
import coke.controller.camp.service.BoardImageService;
import coke.controller.camp.service.BoardService;
import coke.controller.camp.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UploadController {

    private final BoardService boardService;

    private final BoardImageService boardImageService;

    private final S3Uploader s3Uploader;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${coke.controller.upload.path}")
    private String uploadPath;

    private String makeFolder(){

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        log.info("---------------------uploadAjax--------------------");
        log.info("bucket: " + bucket);

        List<UploadResultDTO> uploadResultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles){

            String originalName = uploadFile.getOriginalFilename();

            String fileName = originalName.substring(originalName.lastIndexOf("/") +1);

            log.info("originalName: " + originalName);
            log.info("fileName: " + fileName);

            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            log.info("saveName: " + saveName);

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);

                String thumbnailName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailName);

                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);

                String s3Url = s3Uploader.upload(saveName);
                s3Uploader.upload(thumbnailName);

                uploadResultDTOList.add(new UploadResultDTO(folderPath, uuid , fileName, s3Url));


                log.info("---------------------end for uploadAjax--------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(uploadResultDTOList, HttpStatus.OK);
    }  // uploadResultDTOList

    @GetMapping("/display")
    public ResponseEntity<byte[]> display(String files){

        ResponseEntity<byte[]> result = null;

        try {
            String scrFileName = URLDecoder.decode(files, "UTF-8");

            File file = new File(uploadPath + File.separator + scrFileName);

            log.info(scrFileName);
            log.info(file);

            HttpHeaders headers = new HttpHeaders();

            headers.add("content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return result;

    }

    @PostMapping("/deleteFile")
    public ResponseEntity<Boolean> deleteFile(String files){

        log.info("----delete file for ckeditor -------");

        String srcFile = null;

        files = files.substring(14);

        try {
            srcFile = URLDecoder.decode(files, "UTF-8");

            log.info("srcFile: " + srcFile);

            File file = new File(uploadPath + File.separator + srcFile);

            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @DeleteMapping("/removeFile")
    public ResponseEntity<Boolean>  removeFile(String files){

        log.info("--------removeFile-----------------");

        String srcFile = null;

        try {
            srcFile = URLDecoder.decode(files, "UTF-8");

            log.info(srcFile);

            File file = new File(uploadPath + File.separator + srcFile );

            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/board/images/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardImageDTO>> getImageList(@PathVariable("bno") Long bno){

        log.info("bno: " + bno);

        return new ResponseEntity<>(boardService.getBoardImageList(bno), HttpStatus.OK);

    }

    @DeleteMapping("/removeS3")
    public ResponseEntity<String> removeS3(String files){

        log.info("-----------removeS3-----------------");

        String result = files;

        log.info(files);

        try {
            s3Uploader.removeS3File(files);
            result = "success";
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
