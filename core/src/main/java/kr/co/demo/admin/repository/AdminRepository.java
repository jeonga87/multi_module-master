package kr.co.demo.admin.repository;

import kr.co.demo.admin.domain.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository {

	 /**
	 * 관리자 등록
	 * @param admin
	 * @return int
	 */
	int insertAdmin(Admin admin) throws Exception;

	/**
	 * 관리자 카운트 조회
	 * @param admin
	 * @return int
	 */
	int selectAdminCount(Admin admin);

	/**
	 * 관리자 목록 조회
	 * @param admin
	 * @return 조건에 해당하는 관리자 리스트
	 */
	List<Admin> selectAdminList(Admin admin);

	/**
	 * 관리자 조회
	 * @param id
	 * @return 조건에 해당하는 관리자
	 */
	Admin selectAdmin(Long id);

	/**
	 * 관리자 조회 카운트
	 * @param id
	 * @return 조건에 해당하는 관리자
	 */
	int selectAdminIdCount(String id);

	/**
	 * 관리자 조회
	 * @param id
	 * @return 조건에 해당하는 관리자
	 */
	Admin selectAdminId(String id);

	/**
	 * 관리자 수정
	 * @param admin
	 * @return int
	 */
	int updateAdmin(Admin admin) throws Exception;

	/**
	 * 관리자 삭제
	 * @param admin
	 * @return int
	 */
	int deleteAdmin(Admin admin) throws Exception;

	/**
	 * 관리자 로그 등록
	 * @param admin
	 * @return int
	 */
	int insertAdminLog(Admin admin);
}
