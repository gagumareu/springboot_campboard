package coke.controller.camp.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {

    private String email;
    private String password;
    private String name;
    private boolean del;
    private boolean social;
    private String profileImg;

}
