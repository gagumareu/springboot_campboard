package coke.controller.camp.service;

import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.GearImageDTO;
import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.dto.PageResultDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.GearImage;
import coke.controller.camp.entity.Member;
import coke.controller.camp.repository.GearImageRepository;
import coke.controller.camp.repository.GearRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
                (List<GearImage>) (Arrays.asList((GearImage) arr[2])),
                (Board) arr[3]
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

        Board board = (Board) result.get(0)[3];

        GearDTO gearDTO = entityToDto(gear, member, gearImageList, board);

        return gearDTO;
    }

    @Override
    public void updateState(GearDTO gearDTO) {

        log.info("------gear update state-----");
        log.info(gearDTO.getGno());
        log.info(gearDTO.getState());
        log.info(gearDTO.getBno());

        Optional<Gear> result = gearRepository.findById(gearDTO.getGno());

        Gear gear = result.orElseThrow();

        gear.changeState(gearDTO.getState());

        Board board = Board.builder().bno(gearDTO.getBno()).build();

        gear.changeBno(board);

        log.info("gear: " + gear);

        gearRepository.save(gear);

    }

    @Override
    public void backStateZero(Long bno) {

        log.info("-----------backStateToZero----------");

        Board board = Board.builder().bno(bno).build();

        Optional<Gear> result = gearRepository.getGearByBoard(board);

        if (result.isPresent()){
            Gear gear = result.orElseThrow();
            
            gear.setBoard(null);

            gear.changeState(0);

            log.info(gear);

            gearRepository.save(gear);
        }

    }

    @Override
    public PageResultDTO<GearDTO, Object[]> getListWithPagination(String email, PageRequestDTO pageRequestDTO) {

        log.info("-----getListWithPagination-------");
        log.info(email);
        log.info(pageRequestDTO);

        Function<Object[], GearDTO> fn = (en -> entityToDto(
                (Gear) en[0],
                (Member) en[1],
                (List<GearImage>) (Arrays.asList((GearImage)en[2])),
                (Board) en[3]
        ));

        if (pageRequestDTO.getSort() == ""){
            pageRequestDTO.setSort(null);
        }
        if (pageRequestDTO.getDirection() == ""){
            pageRequestDTO.setDirection(null);
        }

        String dir = pageRequestDTO.getDirection() == null  ? "desc" : pageRequestDTO.getDirection();
        String str = pageRequestDTO.getSort() == null ? "gno" : pageRequestDTO.getSort();

        Sort sort = dir.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, str) : Sort.by(Sort.Direction.DESC, str);

        int page = pageRequestDTO.getPage();

        Pageable pageable = PageRequest.of(page -1, 12, sort);

        Page<Object[]> result = gearRepository.getGearListWithSearching(
                email,
                pageRequestDTO.getSort(),
                pageRequestDTO.getDirection(),
                pageRequestDTO.getKeyword(),
                pageable);

        return new PageResultDTO<>(result, fn);
    }


}
