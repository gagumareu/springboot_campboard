package coke.controller.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    private Long rno;
    private String text;
    private LocalDateTime regDate, modDate;

    private Long bno;

    private String email;
    private String memberName;

}
