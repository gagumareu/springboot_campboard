package coke.controller.camp.gearService;

import coke.controller.camp.dto.*;
import coke.controller.camp.service.PartyService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Log4j2
public class PartyServiceTests {

    @Autowired
    private PartyService partyService;

//    @Test
//    public void getPartyGearByBno(){
//
//        Pageable pageable = PageRequest.of(0, 20, Sort.by("gno").descending());
//
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//
//        PageResultDTO<PartyGearDTO, Object[]> result = partyService.getPartyGearsListWithPagination(160L, pageRequestDTO);
//
//        result.getDtoList().forEach(partyDTO -> {
//            System.out.println(partyDTO);
//        });
//    }

//    @Test
//    public void getApplicant(){
//
//        List<PartyDTO> result = partyService.getApplicantsByBno(210L);
//
//        result.forEach(partyDTO -> {
//            System.out.println(partyDTO);
//        });
//    }

//    @Test
//    public void getBoardListDate(){
//
//        List<BoardDTO> resultList = partyService.getPartiesNBoardsListByEmail("user1@email.com");
//
//        resultList.forEach(objects -> {
//            System.out.println(objects);
//        });
//
//    }
//
//
//    @Test
//    public void getBoardListDateRange(){
//
//        LocalDate start = LocalDate.of(2023,10,01);
//        LocalDate end = LocalDate.of(2024,04,30);
//
//        List<BoardDTO> resultList = partyService.getPartiesNBoardsRangeListByEmail(start, end, "user1@email.com");
//
//        resultList.forEach(objects -> {
//            System.out.println(objects);
//        });
//
//    }
//
//    @Test
//    public void getPartiesAllList(){
//
//        LocalDate start = LocalDate.of(2023,10,01);
//        LocalDate end = LocalDate.of(2024,04,30);
//
//
//        List<PartyDTO> resultList = partyService.getAllPartiesRangeList(start, end);
//
//        resultList.forEach(objects -> {
//            System.out.println(objects);
//        });
//
//    }
//
//    @Test
//    public void getPartyInfo(){
//        PartyDTO partyDTO = partyService.getPartyInfoByBno(216L);
//        System.out.println(partyDTO);
//    }


}
