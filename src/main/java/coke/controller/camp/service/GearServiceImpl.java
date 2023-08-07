package coke.controller.camp.service;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.GearImageDTO;
import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.GearImage;
import coke.controller.camp.entity.Member;
import coke.controller.camp.repository.GearImageRepository;
import coke.controller.camp.repository.GearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class GearServiceImpl implements GearService{

    private final GearRepository gearRepository;
    private final GearImageRepository gearImageRepository;

    @Override
    public Long register(GearDTO gearDTO) {

        log.info("-------------register-------------");

        Map<String, Object> result = dtoToEntity(gearDTO);

        Gear gear = (Gear) result.get("gear");

        gearRepository.save(gear);

        List<GearImage> gearImageList = (List<GearImage>) result.get("imageList");

        log.info("gearImageList: " + gearImageList);


        if (gearImageList != null){
            gearImageList.stream().forEach(gearImage -> {
                log.info("gearImage :" + gearImage );
                gearImageRepository.save(gearImage);
            });
        }

        log.info("getGno: " + gear.getGno());

        return gear.getGno();

    }

    @Override
    public List<GearDTO> getList(String email) {

        List<Object[]> result = gearRepository.getGearByEmailOrderByGnoDesc(email);

        List<GearDTO> gearDTOList = result.stream().map(arr -> entityToDto(
                (Gear) arr[0],
                (Member) arr[1],
                (List<GearImage>) (Arrays.asList((GearImage) arr[2]))
                )

        ).collect(Collectors.toList());

        return gearDTOList;
    }

    @Transactional
    @Override
    public void remove(Long gno) {

        Optional<GearImage> gearImageList = gearImageRepository.findById(gno);

        if (gearImageList != null){
            gearImageRepository.removeByGno(gno);
        }

        gearRepository.deleteById(gno);

    }

    @Transactional
    @Override
    public void modify(GearDTO gearDTO) {

        log.info("-----------gear modify -----------");

        gearImageRepository.removeByGno(gearDTO.getGno());

        Map<String, Object> result = dtoToEntity(gearDTO);

        log.info(result);

        Gear gear = (Gear) result.get("gear");

        List<GearImage> gearImageList = (List<GearImage>) result.get("imageList");

        if(gearImageList != null && gearImageList.size() > 0){
            gearImageList.forEach(gearImage -> {
                gearImageRepository.save(gearImage);
            });
        }

        gearRepository.save(gear);

    }

    @Override
    public List<GearImageDTO> getImagesList(Long gno) {

        List<GearImage> gearImageList = gearImageRepository.getGearImagesByGno(gno);

        return entityImagesToDTO(gearImageList);
    }

    @Override
    public GearDTO getByGno(Long gno) {

        List<Object[]> result = gearRepository.getGearByGno(gno);

        Gear gear = (Gear) result.get(0)[0];

        Member member = (Member) result.get(0)[1];

        List<GearImage> gearImageList = new ArrayList<>();

        result.forEach(arr -> {
            GearImage gearImage = (GearImage) arr[2];
            gearImageList.add(gearImage);
        });

        GearDTO gearDTO = entityToDto(gear, member, gearImageList);

        return gearDTO;
    }
}
