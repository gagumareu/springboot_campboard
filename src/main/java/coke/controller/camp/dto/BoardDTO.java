package coke.controller.camp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String category;
    private LocalDateTime regDate, modDate;

    private String email;
    private String memberName;
    private String profileImg;

//    private Long gno;

    private int replyCount;

    @Builder.Default
    private List<BoardImageDTO> boardImageDTOList = new ArrayList<>();

}
