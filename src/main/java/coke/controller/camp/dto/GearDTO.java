package coke.controller.camp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GearDTO {

    private Long gno;

    @NotEmpty
    private String gname;
    private String brand;
    private String material;
    private String size;
    private String script;
    @NotEmpty
    private String sort;
    private int state;
    private String s3Url;

    @NotEmpty
    private String email;
    private String memberName;
    private String profileImg;

    private Long bno;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    @JsonIgnore
    private LocalDateTime modDate;

    @Builder.Default
    private List<GearImageDTO> gearImageDTOList = new ArrayList<>();

}
