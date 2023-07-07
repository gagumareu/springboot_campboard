package coke.controller.camp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GearDTO {

    private Long gno;
    private String gname;
    private String brand;
    private String material;
    private String size;

    private String email;
    private String memberName;

    private LocalDateTime regDate, modDate;

}
