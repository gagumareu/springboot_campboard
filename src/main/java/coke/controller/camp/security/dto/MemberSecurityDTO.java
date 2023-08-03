package coke.controller.camp.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String email;
    private String password;
    private String memberName;
    private boolean del;
    private boolean social;

    private Map<String, Object> props;

    public MemberSecurityDTO(String username,
                             String password,
                             String memberName,
                             boolean del,
                             boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.email = username;
        this.password = password;
        this.memberName = memberName;
        this.del = del;
        this.social = social;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.email;
    }

}
