package kr.co.demo.admin.service;

import kr.co.demo.admin.domain.Admin;
import kr.co.demo.admin.repository.AdminRepository;
import kr.co.demo.util.Aes256Util;
import kr.co.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Aes256Util aes256Util = new Aes256Util();

	/**
	 * 관리자 등록
	 * @param admin
	 * @return int
	 */
	@Transactional
	public int insertAdmin(Admin admin) throws Exception {
		//암호화
		if(!CommonUtil.isNullEmpty(admin.getPwd())) admin.setPwd(passwordEncoder.encode(admin.getPwd()));
		if(!CommonUtil.isNullEmpty(admin.getPhone())) admin.setPhone(aes256Util.encrypt(admin.getPhone()));
		if(!CommonUtil.isNullEmpty(admin.getEmail())) admin.setEmail(aes256Util.encrypt(admin.getEmail()));

		return adminRepository.insertAdmin(admin);
	}

	/**
	 * 관리자 카운트 조회
	 *
	 * @return 조건에 해당하는 관리자 카운트
	 */
	@Transactional(readOnly = true)
	public int getAdminCount(Admin admin) {
		return adminRepository.selectAdminCount(admin);
	}

	/**
	 * 관리자 리스트 조회
	 *
	 * @return 조건에 해당하는 관리자 리스트
	 */
	@Transactional(readOnly = true)
	public List<Admin> getAdminList(Admin admin) throws Exception {
		List<Admin> adminList = adminRepository.selectAdminList(admin);
		return adminList;
	}

	/**
	 * 관리자 조회
	 *
	 * @return 조건에 해당하는 관리자
	 */
	@Transactional(readOnly = true)
	public Admin getAdmin(Long idx) {
		return adminRepository.selectAdmin(idx);
	}

	/**
	 * 관리자 아이디 카운트
	 *
	 * @return 조건에 해당하는 관리자
	 */
	@Transactional(readOnly = true)
	public int getAdminIdCount(String id) {
		return adminRepository.selectAdminIdCount(id);
	}

	/**
	 * 관리자 아이디 조회
	 *
	 * @return 조건에 해당하는 관리자
	 */
	@Transactional(readOnly = true)
	public Admin getAdminId(String id) {
		return adminRepository.selectAdminId(id);
	}

	/**
	 * 관리자 수정
	 * @param admin
	 * @return int
	 */
	@Transactional
	public void updateAdmin(Admin admin) throws Exception {
		//암호화
		if(!CommonUtil.isNullEmpty(admin.getPwd())) admin.setPwd(passwordEncoder.encode(admin.getPwd()));
		if(!CommonUtil.isNullEmpty(admin.getPhone())) admin.setPhone(aes256Util.encrypt(admin.getPhone()));
		if(!CommonUtil.isNullEmpty(admin.getEmail())) admin.setEmail(aes256Util.encrypt(admin.getEmail()));

		adminRepository.updateAdmin(admin);
	}

	/**
	 * 관리자 삭제
	 * @param admin
	 * @return
	 */
	@Transactional
	public int deleteAdmin(Admin admin) throws Exception {
		return adminRepository.deleteAdmin(admin);
	}

	/**
	 * 관리자 로그 등록
	 * @param admin
	 * @return int
	 */
	@Transactional
	public int insertAdminLog(Admin admin) {
		return adminRepository.insertAdminLog(admin);
	}
}
