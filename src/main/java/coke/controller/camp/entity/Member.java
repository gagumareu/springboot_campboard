package coke.controller.camp.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "roleSet")
public class Member {

    @Id
    private String email;

    private String memberName;

    private String password;

    private boolean del;

    private boolean fromSocial;

    private String profileImg;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String password){
        this.password = password;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeName(String memberName){
        this.memberName = memberName;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean fromSocial){
        this.fromSocial = fromSocial;
    }

    public void changeProfileImg(String profileImg){
        this.profileImg = profileImg;
    }
}
