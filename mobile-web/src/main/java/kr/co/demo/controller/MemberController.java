package kr.co.demo.controller;

import kr.co.demo.member.domain.Member;
import kr.co.demo.member.service.MemberService;
import kr.co.demo.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Class Name : MemberController.java
 * Description : 멤버 컨트롤러 클래스
 * Modification Information
 * Generated : Source Builder
 */

@Controller
@RequestMapping("/member")
public class MemberController extends BaseFormController {

  private static final Logger log = LoggerFactory.getLogger(MemberController.class);

  @Autowired
  private MemberService memberService;

  private final String JSP_PATH = "/member/";

  @RequestMapping({"", "/"})
  public String index() {
    return "redirect:/login";
  }

  @RequestMapping(value = "/info")
  public String info(ModelMap model) {

    String id = SecurityUtils.getCurrentMember().getId();
    Member member = memberService.getMemberById(id);

    if(member == null) { return "redirect:/login"; }

    model.addAttribute("member", member);

    return JSP_PATH + "memberInfo";
  }
}
