package kr.co.demo.admin.domain;

import kr.co.demo.common.Base;
import kr.co.demo.common.Role;
import kr.co.demo.config.Constants;
import kr.co.demo.util.Aes256Util;
import kr.co.demo.util.ValidateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Alias("admin")
@Data
@EqualsAndHashCode(callSuper=false)
public class Admin extends Base implements UserDetails, Serializable {

	/** 관리자 일련번호*/
	private Long idx;

	/** 로그인 계정 아이디 */
	private String id;

	/** 로그인 계정 비밀번호 */
	private String pwd;

	/** 이름 */
	private String name;

	/** 이메일 */
	private String email;

	/** 휴대폰번호 */
	private String phone;

	/** 휴대폰번호1 */
	private String phone1;

	/** 휴대폰번호2 */
	private String phone2;

	/** 휴대폰번호3 */
	private String phone3;

	/** 휴대폰 암호화 복호화 */
	Aes256Util aes256Util = new Aes256Util();

	/** phone1,2,3 합쳐서 phone에 넣기 */
	public String joinPhone() {
		if(this.phone1 != null && this.phone2 != null && this.phone3 != null) {
			String phoneTemp = this.phone1 + "-" + this.phone2 + "-" + this.phone3;
			if(ValidateUtil.isPhone(phoneTemp)) {
				this.phone = aes256Util.encrypt(phoneTemp);
			} else {
				this.phone = null;
			}
		}
		return this.phone;
	}

	/** phone "-"으로 나눠서 phone1,2,3에 넣기 */
	public void splitPhone() {
		if(this.phone != null) {
			this.phone = aes256Util.decrypt(this.phone);
			String[] phoneArr = this.phone.split("\\-");
			this.phone1 = phoneArr[0];
			this.phone2 = phoneArr[1];
			this.phone3 = phoneArr[2];
		}
	}

	/** 사용여부 */
	private String useYn;

	/** 삭제여부 */
	private String delYn;

	/** 생성자 */
	private String createdBy;

	/** 등록일 */
	private Date createdDt;

	/** 수정자 */
	private String modifiedBy;

	/** 수정일 */
	private Date modifiedDt;

	/** 아이피 */
	private String ip;

	private Set<Role> authorities = new HashSet();

	@Override
	public boolean isEnabled() {
		return "Y".equals(useYn);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		authorities.add(new Role(Constants.AuthoritiesConsf.ADMIN));
		return authorities;
	}

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public String getPassword() {
		return pwd;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
