package coke.controller.camp.service;

import coke.controller.camp.dto.*;
import coke.controller.camp.entity.*;
import coke.controller.camp.repository.PartyGearRepository;
import coke.controller.camp.repository.PartyRepository;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService{

    private final PartyRepository partyRepository;

    private final PartyGearRepository partyGearRepository;

    private final ModelMapper modelMapper;

    @Override
    public Long save(PartyDTO partyDTO) {

        log.info("------save------");
        log.info(partyDTO);

        Party party = dtoToEntity(partyDTO);

        Party result = partyRepository.save(party);

        return result.getPno();
    }

    @Override
    public PageResultDTO<PartyDTO, Object[]> getPartyByBnoWithList(Long bno, PageRequestDTO pageRequestDTO) {

       log.info("--------getPartyByBnoWithList---------");
       log.info(bno);
       log.info(pageRequestDTO);

        Function<Object[], PartyDTO> fn = (en -> entityToDTO(
                (Party) en[0],
                (Member) en[1],
                (Board) en[2],
                (Gear) en[3],
                (GearImage) en[4]
        ));

        if (pageRequestDTO.getSort() == ""){
            pageRequestDTO.setSort(null);
        }
        if (pageRequestDTO.getDirection() == ""){
            pageRequestDTO.setDirection(null);
        }

        String dir = pageRequestDTO.getDirection() == null ? "ASC" : pageRequestDTO.getDirection();
        String str = pageRequestDTO.getSort() == null ? "email" : pageRequestDTO.getSort();

        Sort sort = dir.equalsIgnoreCase("ASC") ?
                Sort.by(Sort.Direction.ASC, str) : Sort.by(Sort.Direction.DESC, str);

        int page = pageRequestDTO.getPage();

        Pageable pageable = PageRequest.of(page -1, 20, sort);

        Page<Object[]> result = partyRepository.getPartyMemberWithGears(
                bno,
                pageRequestDTO.getDirection(),
                pageRequestDTO.getSort(),
                pageRequestDTO.getKeyword(),
                pageable);

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<PartyGearDTO, Object[]> getPartyGearsListWithPagination(Long bno, PageRequestDTO pageRequestDTO) {

        log.info("--------getPartyGearsListWithPagination---------");
        log.info(bno);
        log.info(pageRequestDTO);

        Function<Object[], PartyGearDTO> fn = (en -> PartyGearEntityToDTO(
                (PartyGear) en[0],
                (Member) en[1],
                (Gear) en[2],
                (GearImage) en[3]
        ));

        if (pageRequestDTO.getSort() == ""){
            pageRequestDTO.setSort(null);
        }
        if (pageRequestDTO.getDirection() == ""){
            pageRequestDTO.setDirection(null);
        }

        String dir = pageRequestDTO.getDirection() == null ? "ASC" : pageRequestDTO.getDirection();
        String str = pageRequestDTO.getSort() == null ? "email" : pageRequestDTO.getSort();

        Sort sort = dir.equalsIgnoreCase("ASC") ?
                Sort.by(Sort.Direction.ASC, str) : Sort.by(Sort.Direction.DESC, str);

        int page = pageRequestDTO.getPage();

        Pageable pageable = PageRequest.of(page -1, 20, sort);

        Page<Object[]> result = partyRepository.getPartyGearList(
                bno,
                pageRequestDTO.getDirection(),
                pageRequestDTO.getSort(),
                pageRequestDTO.getKeyword(),
                pageable);

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public int checkAvailableUser(Long bno, String email) {

        log.info("--------checkAvailableUser------------");
        log.info(bno);
        log.info(email);

        int count = partyRepository.getPartyByBnoAndEmail(bno, email);

        return count;
    }

    @Transactional
    @Override
    public String dropOut(Long bno, String email) {

        log.info("--------dropOut from a party-------------");
        log.info(bno);
        log.info(email);

       partyGearRepository.deleteAllByBnoAndEmail(bno, email);

        String result = "";

       int count = partyRepository.dropOutFromParty(bno, email);
       log.info("count: " + count);

      if (count == 1){
          result = "result_success";
      }else {
          result = "result_fail";
      }

        return result;
    }

    @Override
    public String getLocationByBno(Long bno) {

        log.info(bno);

        String location = partyRepository.getLocationByBno(bno);

        log.info(location);

        return location;
    }

    @Override
    public void removePartiesByBno(Long bno) {
        log.info(bno);
        partyRepository.deletePartiesByBno(bno);
    }

    @Override
    public List<PartyDTO> getPartiesByBno(Long bno) {

        log.info("----getPartiesByBno----");

        List<Party> parties = partyRepository.getPartiesByBno(bno);

        return parties.stream().map(party -> standardEntityToDTO(party)).collect(Collectors.toList());
    }

    @Override
    public List<PartyDTO> getApplicantsByBno(Long bno) {

        log.info("---------getApplicantsByBno---------");

        List<Object[]> resultList = partyRepository.getApplicantsByBno(bno);

        List<PartyDTO> list = resultList.stream().map(objects -> applicantsEntityToDTO((Party) objects[0], (Member) objects[1])).collect(Collectors.toList());

        return list;
    }

    @Override
    public Long getCountingApplicant(Long bno) {

        return partyRepository.getPartyCountingApplicantByBno(bno);
    }

    @Override
    public PartyDTO getParty(Long bno) {

        Party result = partyRepository.getParty(bno);

        PartyDTO partyDTO = standardEntityToDTO(result);

        return partyDTO;
    }

    @Override
    public List<BoardDTO> getPartiesNBoardsListByEmail(String email) {

        List<Object[]> resultList = partyRepository.getPartiesBoardListByEmail(email);

        return resultList.stream().map(objects -> partyNBoardEntityToDTO(
                (Long) objects[0],
                (Party) objects[1],
                (Board) objects[2])).collect(Collectors.toList());
    }

    @Override
    public List<BoardDTO> getPartiesNBoardsRangeListByEmail(LocalDate start, LocalDate end, String email) {

        List<Object[]> resultList = partyRepository.getPartiesBoardListByDateRangeNEmail(start, end, email);

//        List<BoardDTO> list = resultList.stream().map(objects -> modelMapper.map(objects, BoardDTO.class)).collect(Collectors.toList());
//
//        return list;

        return resultList.stream().map(objects -> partyNBoardEntityToDTO(
                (Long) objects[0],
                (Party) objects[1],
                (Board) objects[2])).collect(Collectors.toList());
    }

    @Override
    public List<PartyDTO> getAllPartiesRangeList(LocalDate start, LocalDate end) {

        List<Object[]> resultList = partyRepository.getPartiesAllListWithRange(start, end);

        List<PartyDTO> list = resultList.stream().map(objects -> partRangeEntityToDTO(
                (Party) objects[0],
                (Long) objects[1],
                (String) objects[2],
                (String) objects[3],
                (String) objects[4]
        )).collect(Collectors.toList());

        return list;
    }

    @Override
    public PartyDTO getPartyInfoByBno(Long bno) {

        Party party = partyRepository.getPartyInfoByBno(bno);

        PartyDTO partyDTO = standardEntityToDTO(party);

        return partyDTO;
    }


}
