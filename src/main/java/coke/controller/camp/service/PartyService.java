package coke.controller.camp.service;

import coke.controller.camp.dto.*;
import coke.controller.camp.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface PartyService {

    Long save(PartyDTO partyDTO);
    PageResultDTO<PartyDTO, Object[]> getPartyByBnoWithList(Long bno, PageRequestDTO pageRequestDTO);
    PageResultDTO<PartyGearDTO, Object[]> getPartyGearsListWithPagination(Long bno, PageRequestDTO pageRequestDTO);
    int checkAvailableUser(Long bno, String email);
    String dropOut(Long bno, String email);
    String getLocationByBno(Long bno);
    void removePartiesByBno(Long bno);
    List<PartyDTO> getPartiesByBno(Long bno);
    List<PartyDTO> getApplicantsByBno(Long bno);
    Long getCountingApplicant(Long bno);
    PartyDTO getParty(Long bno);
    List<BoardDTO> getPartiesNBoardsListByEmail(String email);
    List<BoardDTO> getPartiesNBoardsRangeListByEmail(LocalDate start, LocalDate end, String email);
    List<PartyDTO> getAllPartiesRangeList(LocalDate start, LocalDate end);
    PartyDTO getPartyInfoByBno(Long bno);
    default Party dtoToEntity(PartyDTO partyDTO){

        Party party = Party.builder()
                .pno(partyDTO.getPno())
                .board(Board.builder().bno(partyDTO.getBno()).build())
                .member(Member.builder().email(partyDTO.getEmail()).build())
                .location(partyDTO.getLocation())
                .appointment(partyDTO.getAppointment())
                .person(partyDTO.getPerson())
                .build();
        return party;
    }

    default PartyDTO entityToDTO(Party party, Member member, Board board, Gear gear, GearImage gearImage){

        PartyDTO partyDTO = PartyDTO.builder()
                .pno(party.getPno())
                .bno(board.getBno())
                .location(party.getLocation())
                .appointment(party.getAppointment())
                .person(party.getPerson())
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .profileImg(member.getProfileImg())
                .build();

        if (gear != null){
            partyDTO.setGno(gear.getGno());
            partyDTO.setGname(gear.getGname());
            partyDTO.setState(gear.getState());
            partyDTO.setSort(gear.getSort());
            partyDTO.setBrand(gear.getBrand());
        }
        if (gearImage != null){
            partyDTO.setIno(gearImage.getIno());
            partyDTO.setFileName(gearImage.getFileName());
            partyDTO.setS3Url(gearImage.getS3Url());
        }

        return partyDTO;

    }

    default PartyGearDTO PartyGearEntityToDTO(PartyGear partyGear, Member member, Gear gear, GearImage gearImage){

        PartyGearDTO partyGearDTO = PartyGearDTO.builder()
                .pgno(partyGear.getPgno())
                .gno(gear.getGno())
                .gname(gear.getGname())
                .brand(gear.getBrand())
                .sort(gear.getSort())
                .state(String.valueOf(gear.getState()))
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .profileImg(member.getProfileImg())
                .build();


        if (gearImage != null){
            partyGearDTO.setS3Url(gearImage.getS3Url());
        }
        return partyGearDTO;
    }

    default PartyDTO standardEntityToDTO(Party party){

        PartyDTO partyDTO = PartyDTO.builder()
                .pno(party.getPno())
                .appointment(party.getAppointment())
                .location(party.getLocation())
                .person(party.getPerson())
                .build();
        return partyDTO;
    }

    default PartyDTO applicantsEntityToDTO(Party party, Member member){

        PartyDTO partyDTO = PartyDTO.builder()
                .pno(party.getPno())
                .appointment(party.getAppointment())
                .person(party.getPerson())
                .location(party.getLocation())
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .s3Url(member.getProfileImg())
                .build();
        return partyDTO;
    }

    default BoardDTO partyNBoardEntityToDTO(Long bno, Party party, Board board){

        BoardDTO boardDTO = BoardDTO.builder()
                .location(party.getLocation())
                .appointment(party.getAppointment())
                .person(party.getPerson())
                .bno(bno)
                .title(board.getTitle())
                .category(board.getCategory())
                .build();

        return boardDTO;
    }

    default PartyDTO partRangeEntityToDTO(Party party, Long bno, String title, String category, String s3Url){

        PartyDTO partyDTO = PartyDTO.builder()
                .pno(party.getPno())
                .location(party.getLocation())
                .appointment(party.getAppointment())
                .person(party.getPerson())
                .bno(bno)
                .category(category)
                .title(title)
                .s3Url(s3Url)
                .build();

        return partyDTO;
    }


}
