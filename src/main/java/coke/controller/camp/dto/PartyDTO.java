package coke.controller.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyDTO {

    // Party
    private Long pno;
    private Long bno;
    private String location;
    private LocalDate appointment;
    private int person;

    // Member
    private String email;
    private String memberName;
    private String profileImg;

    // Gear
    private Long gno;
    private String gname;
    private String brand;
    private int state;
    private String sort;

    // Board
    private String title;
    private String category;

    // Board Image
    private Long ino;
    private String fileName;
    private String s3Url;

    private int counting;


}
