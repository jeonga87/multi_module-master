package kr.co.demo.controller;

import kr.co.demo.admin.domain.Admin;
import kr.co.demo.admin.service.AdminService;
import kr.co.demo.common.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Class Name : AdminController.java
 * Description : admin 컨트롤러 클래스
 * Modification Information
 */

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseFormController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  @Autowired
  private AdminService adminService;

  private final String JSP_PATH = "/admin/";

   /**
   * admin 리스트
   * @param admin
   * @param model
   * @return
   */
  @RequestMapping(value = "/list")
  public String list(Admin admin, ModelMap model) throws Exception {

    admin.setOrderKey("ADMIN_IDX");
    admin.setOrderValue("DESC");

    int totalCount = adminService.getAdminCount(admin);

    Pagination pagination = new Pagination(admin.getPageNo(), admin.getDataPerPage(), totalCount);
    admin.setFromNo(pagination.getFromNo());
    admin.setDataPerPage(pagination.getDataPerPage());

    List<Admin> adminList = adminService.getAdminList(admin);

    setRowNums(adminList, totalCount, pagination.getFromNo());

    model.addAttribute("adminList", adminList);
    model.addAttribute("totalCount", totalCount);
    model.addAttribute("pagination", pagination);
    return JSP_PATH + "adminList";
  }

   /**
   * admin 입력 폼
   * @param
   * @param
   * @return
   */
  @RequestMapping(value = "/new", method = RequestMethod.GET)
  public String newAdmin() {
    return JSP_PATH + "adminDetail";
  }

  /**
   * 관리자 아이디 중복확인
   * @param id
   * @param request
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/adminId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Integer> adminId(@RequestParam(value="id") String id, HttpServletRequest request) throws Exception {
    int rlt = adminService.getAdminIdCount(id);
    return createResponseEntity(true, rlt);
  }

   /**
   * admin 입력 프로세스
   * @param admin
   * @return
   */
  @RequestMapping(value = "/insert", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> insert(@RequestBody Admin admin) throws Exception {
    admin.joinPhone();
    adminService.insertAdmin(admin);
    return createResponseEntity(true);
  }

   /**
   * admin 수정 폼
   * @param idx
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/modify/{idx}", method = {RequestMethod.GET, RequestMethod.POST})
  public String modifyAdmin(@PathVariable("idx") Long idx, Admin adminModify, HttpServletResponse response, ModelMap model) throws Exception {
    Admin admin = adminService.getAdmin(idx);

    if (admin == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return null;
    }

    model.addAttribute("admin", admin);

    return JSP_PATH + "adminDetail";
  }

   /**
   * admin 수정 프로세스
   * @param admin
   * @return
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> update(@RequestBody Admin admin) throws Exception {
    admin.joinPhone();
    adminService.updateAdmin(admin);

    return createResponseEntity(true);
  }

  /**
   * admin 삭제 프로세스
   * @param admin
   * @return
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> delete(@RequestBody Admin admin) throws Exception {
    adminService.deleteAdmin(admin);
    return createResponseEntity(true);
  }
}
