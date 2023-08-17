package coke.controller.camp.repository;

import coke.controller.camp.dto.PageRequestDTO;
import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

        List<Object[]> result = repository.getGearByEmailOrderByGnoDesc("udekang@gmail.com");

        System.out.println(result);

        for (Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void get(){

        List<Object[]> result = repository.getGearByGno(49L);

        for (Object[] arr: result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void getList2(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        Page<Object[]> result = repository.getGearListForPagination("udekang2@naver.com", pageable);

        for (Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }

    }

    @Test
    public void searchList(){

        String email = "udekang2@naver.com";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        String types = "n";
        String keyword = "뉴진";


        Page<Object[]> result = repository.getGearListWithSearching(email, types, keyword, pageable);


    }



}
