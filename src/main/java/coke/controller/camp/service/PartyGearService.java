package coke.controller.camp.service;

import coke.controller.camp.dto.PartyGearDTO;
import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.Member;
import coke.controller.camp.entity.PartyGear;

public interface PartyGearService {

    Long register(Long bno, PartyGearDTO partyGearDTO);
    int deleteAllByBno(Long bno);
    int deletePartyGearByGno(Long gno);

    default PartyGear DTOToEntity(Long bno, PartyGearDTO partyGearDTO){

        PartyGear partyGear = PartyGear.builder()
                .board(Board.builder().bno(bno).build())
                .gear(Gear.builder().gno(partyGearDTO.getGno()).build())
                .member(Member.builder().email(partyGearDTO.getEmail()).build())
                .build();
        return partyGear;
    }



}
