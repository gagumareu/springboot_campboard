package coke.controller.camp.repository;

import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GearRepositoryTests {

    @Autowired
    private GearRepository repository;

    @Transactional
    @Test
    public void getList(){

        List<Object[]> result = repository.getGearByEmailOrderByGnoDesc("user3@email.com");

        System.out.println(result);

        for (Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

}
