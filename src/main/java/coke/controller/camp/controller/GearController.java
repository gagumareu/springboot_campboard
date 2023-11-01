package coke.controller.camp.controller;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.GearImageDTO;
import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.dto.PageResultDTO;
import coke.controller.camp.service.GearService;
import coke.controller.camp.util.S3Uploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.security.auth.login.CredentialNotFoundException;
import java.io.File;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/gear/")
public class GearController {

    private final GearService gearService;

    private final S3Uploader s3Uploader;

    @Value("${coke.controller.upload.path}")
    private String uploadPath;



    @PostMapping("")
    public Map<String, Long> registerGear(@Valid @RequestBody GearDTO gearDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws BindException{

        log.info("----------gear register------------");
        log.info(gearDTO);

        if (bindingResult.hasErrors()){
            log.info("-------hasErrors----------");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            throw new BindException(bindingResult);
        }

        Map<String, Long> result = new HashMap<>();

        Long gno = gearService.register(gearDTO);

        result.put("gno", gno);

        return result;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<GearDTO>> getList(@PathVariable("email") String email, PageRequestDTO pageRequestDTO){

        log.info("----------gear getList------------");
        log.info(email);
        log.info(pageRequestDTO);

        return new ResponseEntity<>(gearService.getList(email), HttpStatus.OK);
    }

    @GetMapping(value = "/{email}/{sort}/{direction}/{keyword}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResultDTO<GearDTO, Object[]>> getUserGearsListWithPagination(
            @PathVariable("email") String email,
            @PathVariable("sort") String sort,
            @PathVariable("direction") String direction,
            @PathVariable("keyword") String keyword,
            @PathVariable("page") int page,
            PageRequestDTO pageRequestDTO){

        log.info("------------gear getList With pagination---------------");
        log.info(email);
        log.info(sort);
        log.info(direction);
        log.info(keyword);
        log.info(page);

        pageRequestDTO.setSort(sort);
        pageRequestDTO.setDirection(direction);
        pageRequestDTO.setKeyword(keyword);
        pageRequestDTO.setPage(page);

        log.info(pageRequestDTO);

        PageResultDTO<GearDTO, Object[]> resultDTO = gearService.getListWithPagination(email, pageRequestDTO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }



    @DeleteMapping("/{gno}")
    public ResponseEntity<String> remove(@PathVariable("gno") Long gno){

        log.info("----------gear remove------------");
        log.info(gno);

        List<GearImageDTO> gearImageDTOList = gearService.getImagesList(gno);

        if (gearImageDTOList != null){
//            deleteFiles(gearImageDTOList);
            gearImageDTOList.forEach(gearImageDTO -> {
                String targetName = gearImageDTO.getUuid() + "_" + gearImageDTO.getFileName();
                s3Uploader.removeS3File(targetName);
            });
        }

        gearService.remove(gno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{gno}")
    public ResponseEntity<String> modify(@Valid @RequestBody GearDTO gearDTO, BindingResult bindingResult)throws BindException{

        log.info("----------gear modify------------");
        log.info(gearDTO);

        if(bindingResult.hasErrors()){
            log.info("------has Errors---------------");
            throw new BindException(bindingResult);
        }

        gearService.modify(gearDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }



    public void deleteFiles(List<GearImageDTO> gearImageDTOList){

        log.info("--------------deleteFiles---------------");

        gearImageDTOList.forEach(gearImageDTO -> {

            String folderPath = gearImageDTO.getFolderPath();
            String uuid = gearImageDTO.getUuid();
            String fileName = gearImageDTO.getFileName();

            File file = new File(uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName);
            log.info(file);
            file.delete();

            File thumbnail = new File(file.getParent() + File.separator + "s_" + file.getName());
            log.info(thumbnail);
            thumbnail.delete();

        });
    }

    @GetMapping("/myGear/{gno}")
    public ResponseEntity<GearDTO> getGearByGno(@PathVariable Long gno){

        log.info("-------get gear...--------");

        return new ResponseEntity<>(gearService.getByGno(gno), HttpStatus.OK);
    }

    @GetMapping("/images/{gno}")
    public ResponseEntity<List<GearImageDTO>> getImagesByGno(@PathVariable("gno") Long gno){

        log.info("get gear image list ....");

        return new ResponseEntity<>(gearService.getImagesList(gno), HttpStatus.OK);
    }





//    임시 테스트 용

    @GetMapping("/pagination/{email}/{page}")
    public ResponseEntity<PageResultDTO<GearDTO, Object[]>> getListWithPagination(
            @PathVariable("email") String email,
            @PathVariable("page") int page,
            PageRequestDTO pageRequestDTO){

        log.info("------------gear getList With pagination---------------");
        log.info(email);
        log.info(page);
        log.info(pageRequestDTO);

        pageRequestDTO.setPage(page);

        PageResultDTO<GearDTO, Object[]> resultDTO = gearService.getListWithPagination(email, pageRequestDTO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/pagination/{email}/{page}/{keyword}")
    public ResponseEntity<PageResultDTO<GearDTO, Object[]>> getListWithKeywordAndPagination(
            @PathVariable("email") String email,
            @PathVariable("page") int page,
            @PathVariable("keyword") String keyword,
            PageRequestDTO pageRequestDTO){

        log.info("------------gear getList With keyword and pagination---------------");
        log.info(email);
        log.info(page);
        log.info(pageRequestDTO);

        pageRequestDTO.setPage(page);
        pageRequestDTO.setKeyword(keyword);

        PageResultDTO<GearDTO, Object[]> resultDTO = gearService.getListWithPagination(email, pageRequestDTO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/sort/{email}/{sort}/{direction}")
    public ResponseEntity<PageResultDTO<GearDTO, Object[]>> getListWithSortAndPagination(
            @PathVariable("email") String email,
            @PathVariable("sort") String sort,
            @PathVariable("direction") String direction,
            PageRequestDTO pageRequestDTO){

        log.info("------------gear getList With sort and pagination---------------");
        log.info(email);
        log.info(sort);
        log.info(direction);

        pageRequestDTO.setPage(1);
        pageRequestDTO.setSort(sort);
        pageRequestDTO.setDirection(direction);

        log.info(pageRequestDTO);

        PageResultDTO<GearDTO, Object[]> resultDTO = gearService.getListWithPagination(email, pageRequestDTO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }



}
