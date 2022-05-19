package kr.co.demo.member.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.demo.common.Base;
import kr.co.demo.common.Role;
import kr.co.demo.config.Constants;
import kr.co.demo.util.Aes256Util;
import kr.co.demo.util.DateUtil;
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

@Alias("member")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member extends Base implements UserDetails, Serializable {

  /** 일련번호*/
  private Long idx;

  /** 로그인 계정 아이디 */
  private String id;

  /** 기존 로그인 계정 비밀번호(마이페이지 개인정보 변경 시 사용) */
  private String orgpwd;

  /** 로그인 계정 비밀번호 */
  private String pwd;

  /** 로그인 계정 비밀번호 확인 */
  private String pwdRe;

  /** 이름 */
  private String name;

  /** 생년월일(bar있는거)ex 1999-02-02 */
  private String yearBar;

  /** 생년월일(bar없는거)ex 19990202 */
  private String year;

  /** 생년월일(년도) */
  private String year1;

  /** 생년월일(월) */
  private String year2;

  /** 생년월일(일) */
  private String year3;

  /** year "-"으로 나눠서 year1,2,3에 넣기 */
  public void splitYear() {
    if(this.year != null && DateUtil.isValidDate(this.year, "yyyy-MM-dd")) {
      String[] yearArr = this.year.split("\\-");
      this.year1 = yearArr[0];
      this.year2 = yearArr[1];
      this.year3 = yearArr[2];
    }
  }

  /** year1,2,3 합쳐서 yearBar에 넣기 (바있는거)*/
  public String joinYearBar() {
    if(this.year1 != null && this.year2 != null && this.year3 != null) {
          String yearTemp = this.year1 + "-" + this.year2 + "-" + this.year3;
          if(ValidateUtil.isDateStr(yearTemp)) {
              this.yearBar = yearTemp;
          } else {
      this.yearBar = null;
    }
      }
      return this.yearBar;
  }

  /** year1,2,3 합쳐서 year에 넣기 */
  public String joinYear() {
    if(this.year1 != null && this.year2 != null && this.year3 != null) {
          String yearTemp = this.year1 + this.year2 + this.year3;
    this.year = yearTemp;
    }
      return this.year;
  }

  /** 휴대폰번호 */
  private String phone;

  /** 휴대폰번호1 */
  private String phone1;

  /** 휴대폰번호2 */
  private String phone2;

  /** 휴대폰번호3 */
  private String phone3;

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

  /** 일반전화번호 */
  private String tel;

  /** 일반전화번호1 */
  private String tel1;

  /** 일반전화번호2 */
  private String tel2;

  /** 일반전화번호3 */
  private String tel3;

  /** 휴대폰 암호화 복호화 */
  Aes256Util aes256Util = new Aes256Util();

  /** tel1,2,3 합쳐서 tel에 넣기 */
  public String joinTel() {
    if(this.tel1 != null && this.tel2 != null && this.tel3 != null) {
      String telTemp = this.tel1 + "-" + this.tel2 + "-" + this.tel3;
      if(ValidateUtil.isTel(telTemp)) {
          this.phone = aes256Util.encrypt(telTemp);
      } else {
        this.tel = null;
      }
    }
      return this.tel;
  }

  /** tel "-"으로 나눠서 tel1,2,3에 넣기 */
  public void splitTel() {
    if(this.tel != null) {
      this.phone = aes256Util.decrypt(this.phone);
      String[] telArr = this.tel.split("\\-");
      this.tel1 = telArr[0];
      this.tel2 = telArr[1];
      this.tel3 = telArr[2];
    }
  }

  /** 이메일 */
  private String email;

  /** 이메일1 */
  private String email1;

  /** 이메일2 */
  private String email2;

  /** email1,2 합쳐서 암호화 후 email에 넣기 */
  public String joinEmail() {
    if(this.email1 != null && this.email2 != null) {
      String emailTemp = this.email1 + "@" + this.email2;
      if(ValidateUtil.isEmail(emailTemp)) {
        this.email = aes256Util.encrypt(emailTemp);
      }
    }
    return this.email;
  }

  /** email1,2 합쳐서 암호화 없이 email에 넣기 */
  public String joinEmailWithoutEncrypt() {
    if(this.email1 != null && this.email2 != null) {
      String emailTemp = this.email1 + "@" + this.email2;
      if(ValidateUtil.isEmail(emailTemp)) {
        this.email = emailTemp;
      }
    }
    return this.email;
  }

  /** email "-"으로 나눠서 email1,2 에 넣기 */
  public void splitEmail() {
    if(this.email != null) {
      this.email = aes256Util.decrypt(this.email);
      String[] emailArr = this.email.split("\\@");
      this.email1 = emailArr[0];
      this.email2 = emailArr[1];
    }
  }

  /** 이메일 수신여부 */
  private String emailYn;

  /** ci */
  private String ci;

  /** di */
  private String di;

  /** 가입경로 (사용자 입력이 아닌 PC/Mobile 여부) */
  private String joinCourse;

  /** (회원가입용) 서비스 이용약관 동의 여부 */
    private String agree1;

  /** (회원가입용) 개인정보 수집이용 동의 여부 */
    private String agree2;

  /** (회원가입용) 개인정보 취급 위탁 안내 동의 여부 */
    private String agree3;

  /** (회원가입용) 개인정보 취급 민감정보 수집 · 이용 */
  private String agree4;

  /** (회원가입용) 개인정보 제3자 제공 및 활용*/
  private String agree5;

  /** 휴면계정여부 */
  private String dormancyYn;

  /** 휴면계정일 */
  private String dormancyDt;

  /** 삭제여부 */
  private String delYn;

  /** 삭제일 */
  private String delDt;

  /** 마지막 로그인 */
  private String lastLoginDt;

  /** 생성자 */
  private String createdBy;

  /** 등록일 */
  private String createdDt;

  /** 수정자 */
  private String modifiedBy;

  /** 수정일 */
  private Date modifiedDt;

  /** 아이피 */
  private String ip;

  /** (본인인증용) 본인인증 정보가 저장된 세션의 키 */
  private String certId;

  /** (본인인증용) 인증 결과 메세지(동일 휴대폰번호 사용 회원이 여럿인 경우 일때만 사용) */
  private String certMsg;

  private Set<Role> authorities = new HashSet();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    authorities.add(new Role(Constants.AuthoritiesConsf.MEMBER));
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

  /**
   * 탈퇴 계정이 아닌지 여부
   * @return
   */
  @Override
  public boolean isEnabled() {
    // 탈퇴여부(delYn)이 Y이면 탈퇴 상태 (로그인 불가)
    return "Y".equals(delYn) == false;
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
