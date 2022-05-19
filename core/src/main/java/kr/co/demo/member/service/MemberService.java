package kr.co.demo.member.service;

import kr.co.demo.member.domain.Member;
import kr.co.demo.member.repository.MemberRepository;
import kr.co.demo.security.SecurityUtils;
import kr.co.demo.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service()
public class MemberService {

	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Aes256Util aes256Util = new Aes256Util();

	/**
	 * 사용자 카운트 조회
	 *
	 * @return 조건에 해당하는 사용자 카운트
	 */
	@Transactional(readOnly = true)
	public int getMemberCount(Member member) {
		return memberRepository.selectMemberCount(member);
	}

	/**
	 * 사용자 카운트 조회
	 *
	 * @return 조건에 해당하는 사용자 카운트
	 */
	@Transactional(readOnly = true)
	public int selectPointBatchCount(Member member) {
		return memberRepository.selectPointBatchCount(member);
	}

	/**
	 * 사용자 리스트 조회
	 *
	 * @return 조건에 해당하는 사용자 리스트
	 */
	@Transactional(readOnly = true)
	public List<Member> getMemberList(Member member) {
		return memberRepository.selectMemberList(member);
	}

	/**
	 * 사용자 조회
	 *
	 * @return 조건에 해당하는 사용자
	 */
	@Transactional(readOnly = true)
	public Member getMember(Long idx) {
		return memberRepository.selectMember(idx);
	}

	/**
	 * 사용자 아이디 카운트
	 *
	 * @return 조건에 해당하는 사용자
	 */
	@Transactional(readOnly = true)
	public int getMemberIdCount(String id) {
		return memberRepository.selectMemberIdCount(id);
	}

	/**
	 * 사용자 아이디 조회
	 * (휴면, 탈퇴 사용자 다 조회함)
	 *
	 * @return 조건에 해당하는 사용자
	 */
	@Transactional(readOnly = true)
	public Member getMemberId(String id) {
		return memberRepository.selectMemberId(id);
	}

	/**
     * Member 엑셀 리스트 다운로드
     *
     * @return 조건에 해당하는 Member
     */
    @Transactional(readOnly = true)
    public List<?> getMemberListExcel(Member member) {
        return memberRepository.selectMemberListExcel(member);
    }

    /**
     * Member Delete 엑셀 리스트 다운로드
     *
     * @return 조건에 해당하는 Member
     */
    @Transactional(readOnly = true)
    public List<?> getMemberDeleteListExcel(Member member) {
        return memberRepository.selectMemberDeleteListExcel(member);
    }

    /**
     * Member  엑셀 리스트 다운로드
     *
     * @return 조건에 해당하는 Member
     */
    @Transactional(readOnly = true)
    public List<?> getMemberDomarncyListExcel(Member member) {
        return memberRepository.selectMemberDomarncyListExcel(member);
    }

	/**
	 * 사용자 로그인 아이디, 비밀번호로 사용자 일치여부 확인
	 * (마이페이지 > 개인정보 관리에서 사용)
	 * @param id	사용자 로그인 아이디
	 * @param pwd	사용자 로그인 비밀번호
	 * @return
	 */
	public Member getMemberByIdpwd(String id, String pwd) {
		Member resultMember = null;

		if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(pwd)) {
			Member member = getMemberId(id);
			if(member != null && StringUtils.isNotEmpty(member.getId()) && passwordEncoder.matches(pwd, member.getPwd())) {
				resultMember = new Member();

				// 보안상 필요한 정보만 추려서 전달한다.
				resultMember.setIdx(member.getIdx());				// idx
			}
		}

		return resultMember;
	}

	/**
	 * 사용자 로그인 아이디로 사용자 정보 조회
	 * (마이페이지 > 개인정보 관리에서 사용)
	 * @param id	사용자 로그인 아이디
	 * @return
	 */
	public Member getMemberById(String id) {
		Member resultMember = null;

		if ( StringUtils.isNotEmpty(id) ) {
			Member member = getMemberId(id);

			if ( member != null && StringUtils.isNotEmpty(member.getId()) ) {
				resultMember = new Member();

				// 보안상 필요한 정보만 추려서 전달한다.
				resultMember.setIdx(member.getIdx());				// idx
				resultMember.setId(member.getId());					// 아이디
				resultMember.setName(member.getName());				// 이름
				resultMember.setYear(member.getYear());				// 생년월일[YYYY-MM-DD]
				member.splitPhone();
				resultMember.setPhone1(member.getPhone1());			// 휴대전화1
				resultMember.setPhone2(member.getPhone2());			// 휴대전화2
				resultMember.setPhone3(member.getPhone3());			// 휴대전화3
				member.splitEmail();
				resultMember.setEmail1(member.getEmail1());			// 이메일1
				resultMember.setEmail2(member.getEmail2());			// 이메일2
				resultMember.setEmailYn(member.getEmailYn());		// 이메일 수신여부
			}
		}

		return resultMember;
	}

	/**
	 * 사용자 이름, 이메일, 휴대전화로 아이디 찾기
	 * @param member
	 * @return 마스킹된 아이디
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> getMemberFindId(Member member) {
		String maskedId = null;
		Member findMember = new Member();	// DB 검색 정보를 넣을 Member 객체

		String name = StringUtils.trim(member.getName()); // 이름
		if(StringUtils.isEmpty(name)) {
			return createResultMap(false, "이름을 입력해 주세요.");
		}
		findMember.setName(name);

		String email = member.joinEmail();	// 이메일1,2 합쳐서 이메일에 넣기
		findMember.setEmail(email);
		if(email == null) {
			return createResultMap(false, "이메일을 입력해 주세요.");
		}

		List<Member> memberList = memberRepository.selectMemberForFindId(findMember);
		if(memberList != null && memberList.size() > 0) {
			String id = memberList.get(0).getId();
			maskedId = getMaskedId(id);
		}
		if(maskedId == null) {
			return createResultMap(false, "fail");
		}
		return createResultMap(true, "success");
	}

	/**
	 * 사용자 계정 아이디 마스킹 처리
	 * @param id
	 * @return maskedId
	 */
	public String getMaskedId(String id) {
		/*
		 * id 뒤 두자리를 마스킹 처리
		 */
		int length = id.length();
		return id.substring(0,length-2) + "**";
	}

	/**
	 * 사용자 가입(등록)
	 * @param certInfo 본인인증 세션 정보
	 * @param member 입력받은 회원 가입 정보
	 * @return
	 */
	@Transactional
	public Map<String, Object> joinMember(Map<String, Object> certInfo, Member member) throws Exception {

		/*********************************************************
		   ##### 사용자 가입 프로세스 #####
		   1. 본인인증 세션 정보 확인
		   2. 입력받은 회원 가입 정보 유효성 확인 및 가공
		   3. 본인인증 세션 정보를 입력받은 회원 가입 정보에 병합
		   4. DB 저장
		   5. 가입 안내 메일 발송
		 *********************************************************/

		// 1. 본인인증 세션 정보 확인
		boolean isCertificate = certInfo != null && StringUtils.isNotEmpty((String) certInfo.get("name"));	// 이름까지만 확인한다.
		if(isCertificate == false) {	// 본인인증 정보 없음
			return createResultMap(false, "본인인증 정보가 없습니다. 회원가입을 처음부터 다시 진행해 주세요.");
		}
		// 본인인증 고유 키가 join으로 시작해야 함.
		/*String certId = (String) certInfo.get("certId");
		if(StringUtils.isEmpty(certId) || certId.startsWith("join") == false) {
			return createResultMap(false, "잘못된 본인인증 정보입니다. 회원가입을 처음부터 다시 진행해 주세요.");
		}
*/
		// 2. 본인인증 세션 정보 & 입력받은 회원 가입 정보 유효성 확인 및 새로운 Member 객체에 넣음
		Member joinMember = new Member();	// DB에 입력할 정보를 넣을 Member 객체

		String name = StringUtils.trim(member.getName());		// 이름
		member.joinYear();
		member.joinYearBar();
		String year = StringUtils.trim(member.getYear());		// 생년월일(YYYYMMDD)
		String yearBar = StringUtils.trim(member.getYearBar());		// 생년월일(YYYY-MM-DD)
		if(StringUtils.isEmpty(name) || name.equals(certInfo.get("name")) == false
				|| StringUtils.isEmpty(year) || year.equals(certInfo.get("birth")) == false) {
			// 이름, 생년월일이 없거나 본인인증 정보의 이름, 생년월일과 불일치
			return createResultMap(false, "본인인증 정보가 잘못되었습니다. 회원가입을 처음부터 다시 진행해 주세요.");
		}
		joinMember.setName(name);
		joinMember.setYear(yearBar);
		// 그 외 본인인증 정보 세팅
		joinMember.setPhone1(member.getPhone1());	// 휴대전화1
		joinMember.setPhone2(member.getPhone2());	// 휴대전화2
		joinMember.setPhone3(member.getPhone3());	// 휴대전화3
		String phone = joinMember.joinPhone();
		if(phone == null) {
			return createResultMap(false, "본인인증 휴대전화 정보가 잘못되었습니다. 회원가입을 처음부터 다시 진행해 주세요.");
		}
		joinMember.setCi((String) certInfo.get("ci"));	// CI
		joinMember.setDi((String) certInfo.get("di"));	// DI

		String id = StringUtils.trim(member.getId());			// 아이디
		if(StringUtils.isEmpty(id)) {
			return createResultMap(false, "아이디를 입력해 주세요.");
		}
		if(ValidateUtil.isMemberId(id) == false) {
			return createResultMap(false, "아이디를 형식에 맞게 입력해 주세요.");
		}
		if(getMemberIdCount(id) > 0) {
			return createResultMap(false, "이미 존재하는 아이디 입니다.");
		}
		joinMember.setId(id);

		String pwd = StringUtils.trim(member.getPwd()); 		// 비밀번호
		String pwdRe = StringUtils.trim(member.getPwdRe());		// 비밀번호 확인
		if(StringUtils.isEmpty(pwd)) {
			return createResultMap(false, "비밀번호를 입력해 주세요.");
		}
		if(ValidateUtil.isMemberPw(pwd) == false) {
			return createResultMap(false, "비밀번호를 형식에 맞게 입력해 주세요.");
		}
		if(StringUtils.isEmpty(pwdRe)) {
			return createResultMap(false, "비밀번호 확인을 입력해 주세요.");
		}
		if(pwd.equals(pwdRe) == false) {
			return createResultMap(false, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}
		// 비밀번호 암호화
		joinMember.setPwd(passwordEncoder.encode(pwd));

		String email1 = StringUtils.trim(member.getEmail1()); // 이메일1
		if(StringUtils.isEmpty(email1)) {
			return createResultMap(false, "이메일 아이디를 입력해 주세요.");
		}
		String email2 = StringUtils.trim(member.getEmail2()); // 이메일2
		if(StringUtils.isEmpty(email2)) {
			return createResultMap(false, "이메일 도메인을 입력해 주세요.");
		}
		String email = member.joinEmail();	// 이메일1,2 합쳐서 이메일에 넣기
		if(email == null) {
			return createResultMap(false, "이메일을 형식에 맞게 입력해 주세요.");
		}
		joinMember.setEmail(email);
		joinMember.setEmailYn(member.getEmailYn());
/*

		String tel1 = StringUtils.trim(member.getTel1());	// 전화번호1
		String tel2 = StringUtils.trim(member.getTel2());	// 전화번호2
		String tel3 = StringUtils.trim(member.getTel3());	// 전화번호3
		if(StringUtils.isNotEmpty(tel1) || StringUtils.isNotEmpty(tel2) || StringUtils.isNotEmpty(tel3)) {
			if(StringUtils.isEmpty(tel1) || StringUtils.isEmpty(tel2) || StringUtils.isEmpty(tel3)) {
				return createResultMap(false, "전화번호를 정확히 입력해 주세요.");
			}
			String tel = member.joinTel();		// 전화번호1,2 합쳐서 전화번호에 넣기
			joinMember.setTel(tel);
		}
*/

		String agree1 = member.getAgree1();	 // 서비스 이용약관 동의여부
		if("Y".equals(agree1) == false) {
			return createResultMap(false, "서비스 이용약관에 동의해 주세요.");
		}
		String agree2 = member.getAgree2();	 // 개인정보 수집이용 동의 여부
		if("Y".equals(agree2) == false) {
			return createResultMap(false, "개인정보 수집이용에 동의해 주세요.");
		}
		String agree3 = member.getAgree3();	 // 개인정보 취급 위탁 안내 동의 여부
		if("Y".equals(agree3) == false) {
			return createResultMap(false, "고유식별정보 수집이용에 동의해 주세요.");
		}
		String agree4 = member.getAgree4();	 // 개인정보 취급 위탁 안내 동의 여부
		if("Y".equals(agree4) == false) {
			return createResultMap(false, "민감정보 수집이용에 동의해 주세요.");
		}
		String agree5 = member.getAgree5();	 // 개인정보 취급 위탁 안내 동의 여부
		if("Y".equals(agree5) == false) {
			return createResultMap(false, "개인정보 제3자 제공 및 활용에 동의해 주세요.");
		}

		// 4. 생성된 Member 객체를 DB 저장
		try {
			// 가입경로(PC/MOBILE) 설정
			String joinCourse = "P";
			if(EnvironmentUtil.isMobileWeb()) {
				joinCourse = "M";
			}
			joinMember.setJoinCourse(joinCourse);
			memberRepository.insertMember(joinMember);	// 사용자(회원) DB Table 입력

		} catch (Exception e) {
			e.printStackTrace();
			return createResultMap(false, "회원 정보 저장 중 오류가 발생했습니다.");
		}

		joinMember = memberRepository.selectMember(joinMember.getIdx());	// 가입된 회원 정보 DB에서 조회
		if(joinMember == null || StringUtils.isEmpty(joinMember.getId())) {
			return createResultMap(false, "회원 정보 저장 중 오류가 발생했습니다.");
		}

		return createResultMap(true, null);
	}

	/**
	 * 사용자 탈퇴
	 * @param member
	 * @return
	 */
	@Transactional
	public Map<String, Object> withdrawMember(Member member) {
		String id;
		Member orgMember = null;
		if(SecurityUtils.isAdmin()) {
			// 관리자는 파라미터에서 아이디/탈퇴사유 가져옴
			id = member.getId();
		} else if(SecurityUtils.isMember()) {
			// 사용자는 로그인 세션에서 아이디 조회
			id = SecurityUtils.getCurrentMember().getId();
			orgMember = getMemberId(id);	// 기존 회원정보 조회
			if(orgMember == null || orgMember.getId() == null) {
				return createResultMap(false, "로그인 사용자 정보를 확인해 주세요.");
			}
			// 사용자 입력 비밀번호 확인
			boolean isEqual = passwordEncoder.matches(member.getPwd(), orgMember.getPwd());
			if(isEqual == false) {
				return createResultMap(false, "비밀번호가 잘못되었습니다.");
			}
		} else {
			return createResultMap(false, "잘못된 접근입니다.");
		}

		// 탈퇴 처리
		Member paramMember = new Member();
		paramMember.setIdx(orgMember.getIdx());

		memberRepository.deleteMember(paramMember);

		// 사용자의 경우 로그아웃 시킨다
		if(SecurityUtils.isMember()) {
			SecurityUtils.signout();
		}

		return createResultMap(true, "탈퇴가 완료되었습니다.");
	}

	/**
	 * 사용자 요청 결과 Map 만들기
	 * @param isSuccess
	 * @param msg
	 * @return
	 */
	private Map<String, Object> createResultMap(boolean isSuccess, String msg) {
		Map<String, Object> resultMap = new HashMap<>();	// 결과코드와 메세지를 전달할 Map
		String resultCode = "fail"; // 결과코드[success=성공|fail=실패]
		String resultMsg = "잘못된 입력값이 있어서 실패하였습니다.";	// 기본 결과 메세지

		if(isSuccess) {
			// 회원 가입 성공
			resultCode = "success";
			if(StringUtils.isNotEmpty(msg)) {
				resultMsg = msg;
			} else {
				resultMsg = "완료되었습니다.";
			}
		} else if(StringUtils.isNotEmpty(msg)) {
			// 회원 가입 실패
			resultMsg = msg;
		}

		resultMap.put("code", resultCode);
		resultMap.put("msg",  resultMsg);
		return resultMap;
	}

	/**
	 * 사용자 수정
	 * @param member
	 * @return int
	 */
	@Transactional
	public void updateMember(Member member) throws Exception {
		if(member.getPwd() != null && member.getPwd().length() != 0) {
			member.setPwd(passwordEncoder.encode(member.getPwd()));
		}
		member.joinPhone();
		member.setEmail(aes256Util.encrypt(member.getEmail()));
		member.joinYearBar();
		member.setYear(member.getYearBar());

		memberRepository.updateMember(member);
	}

	/**
	 * 사용자 마지막 로그인 수정
	 * @param member
	 * @return int
	 */
	@Transactional
	public void updateLoginDateMember(Member member) {
		memberRepository.updateLoginDateMember(filterMemberId(member));
	}

	/**
	 * 사용자 비밀번호 수정
	 * @param member
	 * @return int
	 */
	@Transactional
	public int updatePwdMember(Member member) {
		member.setPwd(passwordEncoder.encode(member.getPwd()));
		return memberRepository.updatePwdMember(filterMemberId(member));
	}

	/**
	 * 사용자 로그 등록
	 * @param member
	 * @return int
	 */
	@Transactional
	public int insertMemberLog(Member member) {
		return memberRepository.insertMemberLog(filterMemberId(member));
	}

	/**
	 * 탈퇴 후 30일 경과 사용자 개인정보 제거 처리 (batch에서 사용)
	 * @return int 삭제 처리된 데이터 수
	 */
	@Transactional
	public int executeMemberWithdraw() {
		String executeName = "탈퇴 후 30일 경과 사용자 개인정보 제거 처리";

		int executeCnt = 0;	// 처리 데이터 수

		// 1. 탈퇴 후 30일 경과 사용자 조회
		List<Member> memberList = memberRepository.selectMemberListDelAfter30Day(new Member());

		// 2. 조회된 대상자 개인정보 제거
		log.info("##### " + executeName + " 시작. " +
				"( " + executeCnt + " / " + memberList.size() + " ) #####");
		for(Member member : memberList) {
			memberRepository.clearMemberPersonalInfo(member);
			executeCnt++;
			log.info("##### " + executeName + " 처리 중. " +
					"( " + executeCnt + " / " + memberList.size() + " ) " +
					"memberIdx: " + member.getIdx() + " / " +
					"memberId: " + member.getId() + " #####");
		}
		log.info("##### " + executeName + " 종료. " +
				"( " + executeCnt + " / " + memberList.size() + " ) #####");

		return executeCnt;
	}

	/**
	 * 1년-1개월(11개월)간 로그인 안한 사용자 휴면계정 처리예정 안내 메일 발송 (batch에서 사용)
	 * @return int 메일 발송한 데이터 수
	 */
	@Transactional
	public int executeSendMemberDormancyMail() {
		String executeName = "1년-1개월(11개월)간 로그인 안한 사용자 휴면계정 처리예정 안내 메일 발송";

		int executeCnt = 0;

		// 1. 1년 이상 로그인 안한 휴면 아닌 사용자 조회
		List<Member> memberList = memberRepository.selectMemberListLoginAfter11Month(new Member());

		// 2. 조회된 대상자 메일 발송
		log.info("##### " + executeName + " 시작. " +
				"( " + executeCnt + " / " + memberList.size() + " ) #####");
		String dormancyDt = DateUtil.moveMonth(DateUtil.getCurrent("yyyyMMdd"), 1);		// 휴면계정 처리예정일(yyyyMMdd)
		for(Member member : memberList) {
			if(EnvironmentUtil.isProd()) {
				// 운영에서만 처리할 것
				MailUtil.sendDormancyInfoMail(member.getEmail(), member.getName(), dormancyDt);	// 휴면계정 처리예정 안내 메일 발송
			}
			executeCnt++;
			log.info("##### " + executeName + " 처리 중. " +
					"( " + executeCnt + " / " + memberList.size() + " ) " +
					"memberIdx: " + member.getIdx() + " / " +
					"memberId: " + member.getId() + " #####");
		}
		log.info("##### " + executeName + " 종료. " +
				"( " + executeCnt + " / " + memberList.size() + " ) #####");

		return executeCnt;
	}

	/**
	 * 1년 이상 로그인 안한 사용자 휴면 처리 (batch에서 사용)
	 * @return int 휴면 처리된 데이터 수
	 */
	@Transactional
	@SuppressWarnings({ "unchecked", "null" })
	@Scheduled(cron="0 0 3 * * ?")
    public int executeMemberDormancy() {
		String executeName = "1년 이상 로그인 안한 사용자 휴면 처리";

		int executeCnt = 0;

		// 1. 1년 이상 로그인 안한 휴면 아닌 사용자 조회
		List<Member> memberList = memberRepository.selectMemberListLoginAfter1Year(new Member());

		// 2. 조회된 대상자 휴면 사용자 테이블(KSF_MEMBER_DORMANCY)로 복사 및 개인정보 제거
		log.info("##### " + executeName + " 시작. " +
				"( " + executeCnt + " / " + memberList.size() + " ) #####");
		String dormancyDt = DateUtil.getCurrentDateTime();		// 휴면처리일
		for(Member member : memberList) {
			member.setDormancyYn("Y");	// 휴면처리
			member.setDormancyDt(dormancyDt);	// 휴면일
			memberRepository.insertMemberDormancy(member);		// 휴면 사용자 테이블 등록
			memberRepository.clearMemberPersonalInfo(member);	// KSF_MEMBER 개인정보 제거
			executeCnt++;
			log.info("##### " + executeName + " 처리 중. " +
					"( " + executeCnt + " / " + memberList.size() + " ) " +
					"memberIdx: " + member.getIdx() + " / " +
					"memberId: " + member.getId() + " #####");
		}
		log.info("##### " + executeName + " 종료. " +
				"( " + executeCnt + " / " + memberList.size() + " ) #####");

		return executeCnt;
    }

	/**
	 * Member 객체 안의 사용자 아이디 값을 관리자/사용자 로그인 상태에 맞게 설정
	 * @param member
	 * @return
	 */
	@Transactional(readOnly = true)
    public Member filterMemberId(Member member) {
		String id = member.getId();
		if(SecurityUtils.isAdmin()) {
			// 관리자 로그인 : 별도 작업 안함
		} else if(SecurityUtils.isMember()) {
			// 사용자 로그인 : 현재 로그인 사용자 아이디로 치환
			id = SecurityUtils.getCurrentMember().getId();
		} else {
			// 로그인 아닌 상태 : null로 치환
			id = null;
		}
		member.setId(id);
		return member;
	}

	/**
	 *  탈퇴 사용자 카운트 조회
	 *
	 * @return 조건에 해당하는 사용자 카운트
	 */
	@Transactional(readOnly = true)
	public int selectMemberDeleteCount(Member member) {
		return memberRepository.selectMemberDeleteCount(member);
	}

	/**
	 * 휴먼 사용자 카운트 조회
	 *
	 * @return 조건에 해당하는 사용자 카운트
	 */
	@Transactional(readOnly = true)
	public int selectMemberDormancyCount(Member member) {
		return memberRepository.selectMemberDormancyCount(member);
	}

	/**
	 * 관리자 삭제
	 * @param member
	 * @return
	 */
	@Transactional
	public int deleteMember(Member member) throws Exception {
		return memberRepository.deleteMember(member);
	}

	/**
	 * 중복 회원가입 방지 ci검색
	 *
	 * @return 조건에 해당하는 사용자 카운트
	 */
	@Transactional(readOnly = true)
	public int selectMemberForFindCi(Member member) {
		return memberRepository.selectMemberForFindCi(member);
	}

}
