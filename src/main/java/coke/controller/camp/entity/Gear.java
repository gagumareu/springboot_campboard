package coke.controller.camp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "member")
public class Gear extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    private String gname;

    private String brand;

    private String size;

    private String material;

    private String sort;

    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
