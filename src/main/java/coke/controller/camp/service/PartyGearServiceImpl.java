package coke.controller.camp.service;

import coke.controller.camp.dto.PartyGearDTO;
import coke.controller.camp.entity.PartyGear;
import coke.controller.camp.repository.PartyGearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PartyGearServiceImpl implements PartyGearService{

    private final PartyGearRepository partyGearRepository;

    @Override
    public Long register(Long bno, PartyGearDTO partyGearDTO) {

        log.info("-----register-------");
        log.info(bno);
        log.info(partyGearDTO);

        PartyGear partyGear = DTOToEntity(bno, partyGearDTO);

        PartyGear result = partyGearRepository.save(partyGear);

        log.info(result);

        return result.getPgno();
    }

    @Override
    public int deleteAllByBno(Long bno) {

        log.info("---deleteAll--");
        log.info(bno);

        return partyGearRepository.deleteAllByBno(bno);


    }

    @Override
    public int deletePartyGearByGno(Long gno) {

        log.info("----deletePartyGearByGno-----");
        log.info(gno);

        return partyGearRepository.deletePartyGearByGno(gno);

    }
}
