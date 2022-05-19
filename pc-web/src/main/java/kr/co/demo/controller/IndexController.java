package kr.co.demo.controller;

import kr.co.demo.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class Name : IndexController.java
 * Description : index 컨트롤러 클래스
 * Modification Information
 * Generated : Source Builder
 */

@Controller
public class IndexController {

  private static final Logger log = LoggerFactory.getLogger(IndexController.class);

  @RequestMapping({"", "/"})
  public String index() {
    return "index";
  }

  /**
   * 로그인 폼
   * @return
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    if(SecurityUtils.isMember()) {
      return "redirect:/";
    }

    return "login";
  }

}