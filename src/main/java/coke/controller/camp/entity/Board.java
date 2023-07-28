package coke.controller.camp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.content = content;
    }
    public void changeCategory(String category){
        this.category = category;
    }
}
