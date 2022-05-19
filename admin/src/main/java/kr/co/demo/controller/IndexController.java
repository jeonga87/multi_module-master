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
    return "redirect:/login";
  }

  @RequestMapping(value = "/dashboard")
  public String dashboard(ModelMap model) {
    return "dashboard";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    if(SecurityUtils.isAdmin()) {
      return "redirect:/dashboard";
    }
    return "login";
  }

  @RequestMapping(value = "/healthChecker")
  public ResponseEntity healthCheker() throws Exception {
    int admin     =  healthCheck(new URL("http://127.0.0.1:8080"));
    int pcweb     =  healthCheck(new URL("http://127.0.0.1:8181"));
    int mobileweb   =  healthCheck(new URL("http://127.0.0.1:8282"));

    //log.debug("admin : " + admin + " | pcweb : " + pcweb + " | mobileweb : " + mobileweb);

    if(admin == 200 && pcweb == 200 && mobileweb == 200) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  public int healthCheck(URL url) {
    int code = 400;

    try {
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setConnectTimeout(30000);
      con.connect();
      code = con.getResponseCode();
      //msg = con.getResponseMessage();
      con.disconnect();

      if(code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM) {
        String redirectedUrl = con.getHeaderField("Location");
        url = new URL(redirectedUrl);
        code = healthCheck(url);
      }

    } catch (Exception e) {
      //code = 400;
      log.debug("healthChek exception : ", e);
    }
    return code;
  }
}