package kr.co.demo.security;

import kr.co.demo.admin.domain.Admin;
import kr.co.demo.member.domain.Member;
import kr.co.demo.member.service.MemberService;
import kr.co.demo.util.ApplicationUtil;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

/**
 * Utility class for Spring Security.
 */
@Component
public final class SecurityUtils {
	public static boolean hasAuthority(String authority) {
		return hasRole(authority);
	}

	public static boolean hasAnyAuthority(String... authorities) {
		return hasAnyRole(authorities);
	}

	public static boolean hasRole(String role) {
		return getAuthorities().contains(role);
	}

	public static boolean hasRole(Role role) {
		return getAuthorities().contains(role.toString());
	}

	public static boolean hasAnyRole(String... roles) {
		Set<String> authorities = getAuthorities();
		for (String role : roles) {
			if (authorities.contains(role)) {
				return true;
			}
		}
		// No roles matches
		return false;
	}

	public static boolean hasAnyRole(Role... roles) {
		Set<String> authorities = getAuthorities();
		for (Role role : roles) {
			if (authorities.contains(role.toString())) {
				return true;
			}
		}
		// No roles matches
		return false;
	}

	public static boolean isAnonymous() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		return AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
	}

	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		return !isAnonymous();
	}

	public static boolean isFullyAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		return !isAnonymous() && !isRememberMe();
	}

	public static boolean isRememberMe() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
	}

	public static boolean isSwitchedUser() {
		return hasRole("ROLE_PREVIOUS_ADMINISTRATOR");
	}

	private static Set<String> getAuthorities() {
		return AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities());
	}

	/**
	 * If the current user has a specific authority (security role).
	 *
	 * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
	 *
	 * @param authority the authorithy to check
	 * @return true if the current user has the authority, false otherwise
	 */
	public static boolean isCurrentUserInRole(String authority) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
			}
		}
		return false;
	}

	/**
	 * Get the login of the current user.
	 *
	 * @return the login of the current user
	 */
	public static UserDetails getCurrentUser() {
		if(isAuthenticated()) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication == null) {
				return null;
			}
			return (UserDetails)authentication.getPrincipal();
		}
		return null;
	}

	/**
	 * Get the login of the current admin user.
	 *
	 * @return the login of the current admin user
	 */
	public static Admin getCurrentAdmin() {
		UserDetails userDetails = getCurrentUser();
		Admin admin = null;
		if(userDetails != null && userDetails instanceof Admin) {
			admin = (Admin) userDetails;
		}
		return admin;
	}

	/**
	 * 현재 로그인 한 user가 관리자(admin)인지 여부
	 *
	 * @return
	 */
	public static boolean isAdmin() {
		Admin currentAdmin = getCurrentAdmin();
		return currentAdmin != null && currentAdmin.getIdx() != null;
	}

	/**
	 * Get the login of the current member user.
	 *
	 * @return the login of the current member user
	 */
	public static Member getCurrentMember() {
		UserDetails userDetails = getCurrentUser();
		Member member = null;
		if(userDetails != null && userDetails instanceof Member) {
			member = (Member) userDetails;
		}
		return member;
	}

	/**
	 * 현재 로그인 한 user가 사용자(member)인지 여부
	 *
	 * @return
	 */
	public static boolean isMember() {
		Member currentMember = getCurrentMember();
		return currentMember != null && currentMember.getIdx() != null;
	}

	/**
	 * 현재 로그인 한 사용자 Session 갱신
	 */
	public static void reloadMember() {
		if(isMember()) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Member currentMember = (Member)authentication.getPrincipal();
			WebApplicationContext wac = ApplicationUtil.getApplicationContext();
			MemberService memberService = (MemberService) wac.getBean("memberService");
			Member member = memberService.getMemberId(currentMember.getId());
		}
	}

	/**
	 * Signin 배치용 관리자
	 */
	public static void signinAdminForBatch() {
		Admin admin = new Admin();
		admin.setId("(SYSTEM_BATCH)");
		admin.setIdx(0L);
		Authentication auth = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	/**
	 * Signin 사용자
	 * (실제 존재하는 사용자만 가능)
	 * @param id	사용자 로그인 아이디
	 */
	public static void signinMember(String id) {
		WebApplicationContext wac = ApplicationUtil.getApplicationContext();
		MemberService memberService = (MemberService) wac.getBean("memberService");
		Member member = memberService.getMemberId(id);
		member.setPwd(null);	// 비밀번호 제거
		Authentication auth = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	/**
	 * Signout
	 */
	public static void signout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

}
