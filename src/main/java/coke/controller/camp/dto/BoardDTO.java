package coke.controller.camp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
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

    @NotEmpty
    @Size(min = 3, max = 500)
    private String title;
    @NotEmpty
    private String content;

    @NotEmpty
    private String category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    @JsonIgnore
    private LocalDateTime modDate;

    @NotEmpty
    private String email;
    private String memberName;
    private String profileImg;

    private int replyCount;

    @Builder.Default
    private List<BoardImageDTO> boardImageDTOList = new ArrayList<>();

    @Builder.Default
    private List<GearImageDTO> gearImageDTOList = new ArrayList<>();

    private LocalDate appointment;
    private String location;
    private int person;
}
