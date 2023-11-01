package coke.controller.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyGearDTO {

    private Long pgno;

    private Long gno;
    private String gname;
    private String brand;
    private String state;
    private String sort;
    private String s3Url;

    private Long bno;

    private String email;
    private String memberName;
    private String profileImg;





}
