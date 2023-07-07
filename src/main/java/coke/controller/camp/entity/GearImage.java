package coke.controller.camp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "gear")
public class GearImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private String folderPath;

    private String uuid;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gear gear;
}
