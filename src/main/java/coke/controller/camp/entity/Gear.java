package coke.controller.camp.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"member", "board"})
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

    private int state;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Board board;

    public void changeState(int state){
        this.state = state;
    }

    public void changeBno(Board board){
        this.board = board;
    }


}
