package coke.controller.camp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private String folderPath;

    private String uuid;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
