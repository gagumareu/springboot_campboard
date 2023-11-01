package coke.controller.camp.controller;

import coke.controller.camp.dto.*;
import coke.controller.camp.service.PartyGearService;
import coke.controller.camp.service.PartyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/party/")
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PartyController {

    private final PartyService partyService;

    private final PartyGearService partyGearService;


    // 참가자 장비 전체리스트
    @GetMapping(value = "/{bno}/{sort}/{direction}/{keyword}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResultDTO<PartyDTO, Object[]>> getPartyMemberGearList(@PathVariable("bno") Long bno,
                                                                                    @PathVariable("sort") String sort,
                                                                                    @PathVariable("direction") String direction,
                                                                                    @PathVariable("keyword") String keyword,
                                                                                    @PathVariable("page") int page,
                                                                                    PageRequestDTO pageRequestDTO){

        log.info("-------------getPartyMemberGearList----------");

        log.info(bno);
        log.info(sort);
        log.info(direction);
        log.info(keyword);
        log.info(page);
        log.info(pageRequestDTO);

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSort(sort);
        pageRequestDTO.setDirection(direction);
        pageRequestDTO.setKeyword(keyword);

        PageResultDTO<PartyDTO, Object[]> resultDTO = partyService.getPartyByBnoWithList(bno, pageRequestDTO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);

    }

    // 참가자가 등록한 장비 리스트 불러오기
    @GetMapping(value = "/gear/{bno}/{sort}/{direction}/{keyword}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResultDTO<PartyGearDTO, Object[]>> getPartyGearList(@PathVariable("bno") Long bno,
                                                                                    @PathVariable("sort") String sort,
                                                                                    @PathVariable("direction") String direction,
                                                                                    @PathVariable("keyword") String keyword,
                                                                                    @PathVariable("page") int page,
                                                                                    PageRequestDTO pageRequestDTO){

        log.info("-------------getPartyGearList----------");

        log.info(bno);
        log.info(sort);
        log.info(direction);
        log.info(keyword);
        log.info(page);
        log.info(pageRequestDTO);

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSort(sort);
        pageRequestDTO.setDirection(direction);
        pageRequestDTO.setKeyword(keyword);

        PageResultDTO<PartyGearDTO, Object[]> resultDTO = partyService.getPartyGearsListWithPagination(bno, pageRequestDTO);

        return new ResponseEntity<>(resultDTO, HttpStatus.OK);

    }


    @PostMapping(value = "/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> joinParty(@RequestBody PartyDTO partyDTO){

        log.info("-------join party--------");
        log.info(partyDTO);

        Long rno = partyService.save(partyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    // 모임에 참가한 여부 확인
    @GetMapping(value = "/{bno}/{currentUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> checkAvailableUser(@PathVariable("bno") Long bno, @PathVariable("currentUser") String email){

        log.info("------------checkAvailableUser-------");
        log.info(bno);
        log.info(email);

        int count = partyService.checkAvailableUser(bno, email);
        log.info(count);

        return new ResponseEntity<Integer>(count, HttpStatus.OK );
    }

    @DeleteMapping(value = "/{bno}/{currentUser}")
    public ResponseEntity<String> dropOut(@PathVariable("bno") Long bno, @PathVariable("currentUser") String email){

        log.info("------------dropOut-------");
        log.info(bno);
        log.info(email);

        String result = partyService.dropOut(bno, email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 참가 장소 불러오기
    @GetMapping(value = "/{bno}")
    public ResponseEntity<String> getLocation(@PathVariable("bno") Long bno){

        log.info("--------get location--------");
        log.info(bno);

        String address = partyService.getLocationByBno(bno);

        log.info(address);

        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping(value = "/gear/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> registerGear(@PathVariable("bno") Long bno, @RequestBody PartyGearDTO partyGearDTO){

        log.info("----------register gear to party gear------------");
        log.info(bno);
        log.info(partyGearDTO);

        Long pgno = partyGearService.register(bno, partyGearDTO);

        return new ResponseEntity<>(pgno, HttpStatus.OK);
    }

    @DeleteMapping(value = "/gear/{gno}")
    public ResponseEntity<String> deletePartyGearByGno(@PathVariable("gno") Long gno){

        log.info("------deletePartyGearByGno------------");
        log.info(gno);

        int count = partyGearService.deletePartyGearByGno(gno);
        log.info(count);

        return count == 1 ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 파티 참가자 리스트
    @GetMapping(value = "/applicants/{bno}")
    public ResponseEntity<List<PartyDTO>> getApplicantsByBno(@PathVariable("bno") Long bno){

        log.info("------getApplicantsByBno------------");
        log.info(bno);

        List<PartyDTO> list = partyService.getApplicantsByBno(bno);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 파티 참가자 수
    @GetMapping(value = "/countingApplicant/{bno}")
    public ResponseEntity<Long> getCountingApplicant(@PathVariable("bno") Long bno){

        return new ResponseEntity<>(partyService.getCountingApplicant(bno), HttpStatus.OK);
    }

    // get party
    @GetMapping(value = "/get/{bno}")
    public ResponseEntity<PartyDTO> getParty(@PathVariable("bno") Long bno){

        log.info("-----get-----");

        return new ResponseEntity<>(partyService.getParty(bno), HttpStatus.OK);
    }

    //  내에 모임 리스트 불러오기
    @GetMapping(value = "/board/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardDTO>> getPartiesNBoardList(Principal principal){

        log.info("------getPartiesNBoardList-----");


        String email = principal.getName();

        return new ResponseEntity<>(partyService.getPartiesNBoardsListByEmail(email), HttpStatus.OK);
    }

    // 선택한 기간 내에 모임 리스트 불러오기
    @GetMapping(value = "/board/range/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardDTO>> getPartiesNBoardRangeList(LocalDate start, LocalDate end, Principal principal){

        log.info("------getPartiesNBoardRangeList-----");

        String email = principal.getName();

        log.info(start + " - " + end);
        log.info(email);

        return new ResponseEntity<>(partyService.getPartiesNBoardsRangeListByEmail(start, end, email), HttpStatus.OK);
    }

    // 현재 날로 부터 6개월 동안 캠핑 모임 게시글 불러오기
    @GetMapping(value = "/rangeList")
    public ResponseEntity<List<PartyDTO>> getPartiesRangeList(LocalDate start, LocalDate end){

        log.info("---------- getPartiesRangeList-----------");
        log.info(start + " - " + end);

        return new ResponseEntity<>(partyService.getAllPartiesRangeList(start, end), HttpStatus.OK);
    }

}
