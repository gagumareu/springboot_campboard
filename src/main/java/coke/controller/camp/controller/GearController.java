package coke.controller.camp.controller;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.GearImageDTO;
import coke.controller.camp.service.GearService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/gear/")
public class GearController {

    private final GearService gearService;

    @Value("${coke.controller.upload.path}")
    private String uploadPath;

    @PostMapping("")
    public ResponseEntity<Long> registerGear(@RequestBody GearDTO gearDTO){

        log.info("----------gear register------------");
        log.info(gearDTO);

        Long gno = gearService.register(gearDTO);

        return new ResponseEntity<>(gno, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<GearDTO>> getList(@PathVariable("email") String email){

        log.info("----------gear getList------------");
        log.info(email);

        return new ResponseEntity<>(gearService.getList(email), HttpStatus.OK);
    }

    @DeleteMapping("/{gno}")
    public ResponseEntity<String> remove(@PathVariable("gno") Long gno){

        log.info("----------gear remove------------");
        log.info(gno);

        List<GearImageDTO> gearImageDTOList = gearService.getImagesList(gno);

        if (gearImageDTOList != null){
            deleteFiles(gearImageDTOList);
        }

        gearService.remove(gno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{gno}")
    public ResponseEntity<String> modify(@RequestBody GearDTO gearDTO){

        log.info("----------gear modify------------");
        log.info(gearDTO);

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

        log.info("get gear...");

        return new ResponseEntity<>(gearService.getByGno(gno), HttpStatus.OK);
    }

    @GetMapping("/images/{gno}")
    public ResponseEntity<List<GearImageDTO>> getImagesByGno(@PathVariable("gno") Long gno){

        log.info("get gear image list ....");

        return new ResponseEntity<>(gearService.getImagesList(gno), HttpStatus.OK);
    }




}
