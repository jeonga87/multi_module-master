package kr.co.demo.member.repository;

import kr.co.demo.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {

	 /**
	 * 사용자 등록
	 * @param member
	 * @return int
	 */
	int insertMember(Member member);

	/**
	 * 사용자 카운트 조회
	 * @param member
	 * @return int
	 */
	int selectMemberCount(Member member);

	/**
	 * 포인트 일괄 지급 카운트 조회
	 * @param member
	 * @return int
	 */
	int selectPointBatchCount(Member member);


	/**
	 * 사용자 목록 조회
	 * @param member
	 * @return 조건에 해당하는 사용자 리스트
	 */
	List<Member> selectMemberList(Member member);

	/**
	 * 사용자 조회
	 * @param idx
	 * @return 조건에 해당하는 사용자
	 */
	Member selectMember(Long idx);

	/**
	 * 사용자 조회 카운트
	 * @param id
	 * @return 조건에 해당하는 사용자
	 */
	int selectMemberIdCount(String id);

	/**
	 * 사용자 조회
	 * @param id
	 * @return 조건에 해당하는 사용자
	 */
	Member selectMemberId(String id);

	/**
	 * 사용자 본인인증 정보로 조회
	 * @param member
	 * @return 조건에 해당하는 사용자
	 */
	Member selectMemberAuthInfo(Member member);

	/**
	 * 사용자 이름, 이메일로 조회
	 * @param member
	 * @return 조건에 해당하는 사용자 리스트
	 */
	List<Member> selectMemberForFindId(Member member);

	/**
	 * 사용자 수정
	 * @param member
	 * @return int
	 */
	int updateMember(Member member);

	/**
	 * 사용자 마지막 로그인 수정
	 * @param member
	 * @return int
	 */
	int updateLoginDateMember(Member member);

	/**
	 * 사용자 비밀번호 수정
	 * @param member
	 * @return int
	 */
	int updatePwdMember(Member member);

	/**
	 * 사용자 delYN = Y update
	 * @param member
	 * @return int
	 */
	int updateDeleteMember(Member member);

	/**
	 * 사용자 삭제
	 * @param member
	 * @return int
	 */
	int deleteMember(Member member);

	/**
	 * 사용자 로그 등록
	 * @param member
	 * @return int
	 */
	int insertMemberLog(Member member);

	/**
	 * 탈퇴 후 30일 경과된 사용자 목록 조회
	 * @return List<Member>
	 */
	List<Member> selectMemberListDelAfter30Day(Member member);

	/**
	 * 사용자 개인정보 제거
	 * @param member
	 * @return int
	 */
	int clearMemberPersonalInfo(Member member);

	/**
	 * 사용자 개인정보를 휴면정보로 부터 복구
	 * @param member
	 * @return int
	 */
	int revertMemberPersonalInfo(Member member);

	/**
	 * 1년 이상 로그인 안한 휴면 아닌 사용자 목록 조회
	 * @return List<Member>
	 */
	List<Member> selectMemberListLoginAfter1Year(Member member);

	/**
	 * 휴면 사용자 조회
	 * @return member
	 */
	Member selectMemberDormancy(Member member);

	/**
	 * 휴면 사용자 본인인증 정보로 조회
	 * @return member
	 */
	Member selectMemberDormancyAuthInfo(Member member);

	/**
	 * 휴면 사용자 등록
	 * @return int
	 */
	int insertMemberDormancy(Member member);

	/**
	 * 휴면 사용자 삭제
	 * @return int
	 */
	int deleteMemberDormancy(Member member);

	/**
	 * 11개월 로그인 안한 휴면 아닌 사용자 목록 조회
	 * @return List<Member>
	 */
    List<Member> selectMemberListLoginAfter11Month(Member member);

    /**
     * 멤버 리스트 엑셀 다운로드
     * @param member
     * @return 조건에 해당하는 관리자 리스트
     */
    List<?> selectMemberListExcel(Member member);

    /**
     * 탈퇴 멤버 리스트 엑셀 다운로드
     * @param member
     * @return 조건에 해당하는 관리자 리스트
     */
    List<?> selectMemberDeleteListExcel(Member member);

    /**
     * 엑셀 다운로드
     * @param member
     * @return 조건에 해당하는 관리자 리스트
     */
    List<?> selectMemberDomarncyListExcel(Member member);

	/**
	 * 삭제 사용자 카운트 조회
	 * @param member
	 * @return int
	 */
	int selectMemberDeleteCount(Member member);

	/**
	 * 휴먼 사용자 카운트 조회
	 * @param member
	 * @return int
	 */
	int selectMemberDormancyCount(Member member);

	/**
	 * 중복 회원가입 방지 ci검색
	 * @param member
	 * @return int
	 */
	int selectMemberForFindCi(Member member);

	/**
	 * ci로 아이디 찾기
	 * @param member
	 * @return 조건에 해당하는 사용자 리스트
	 */
	List<Member> selectMemberForFindIdCi(Member member);

	/**
	 * 사용자 조회(패스워드 변경을 위한 검색 ci포함)
	 * @param member
	 * @return 조건에 해당하는 사용자
	 */
	Member selectMemberCiId(Member member);
}
