package coke.controller.camp.service;

import coke.controller.camp.dto.GearDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GearServiceTests {

    @Autowired
    private GearService gearService;

    @Test
    public void getByGno(){

        GearDTO gearDTO = gearService.getByGno(49L);

        System.out.println(gearDTO);

        gearDTO.getGearImageDTOList().stream().forEach(gearImageDTO -> {
            System.out.println(gearImageDTO);
        });

    }
}
