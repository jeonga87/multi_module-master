package kr.co.demo.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

@Alias("role")
@Data
@EqualsAndHashCode(callSuper=false)
public class Role implements GrantedAuthority {
    private String roleCode;
    private String roleName;

    public Role(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getAuthority() {
        return roleCode;
    }
}
