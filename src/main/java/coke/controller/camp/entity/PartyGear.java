package coke.controller.camp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"board", "gear"})
public class PartyGear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pgno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gear gear;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


}
