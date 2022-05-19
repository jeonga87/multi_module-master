package kr.co.demo.controller;

import kr.co.demo.common.Pagination;
import kr.co.demo.example.domain.Example;
import kr.co.demo.example.service.ExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Class Name : ExampleController.java
 * Description : 예제 게시판 컨트롤러 클래스
 * Modification Information
 */

@Controller
@RequestMapping("/example")
public class ExampleController extends BaseFormController {

  private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

  @Autowired
  private ExampleService exampleService;

  private final String JSP_PATH = "/example/";

  /**
   * 예제 게시판 리스트
   * @param example
   * @param model
   * @return
   */
  @RequestMapping(value = "/list")
  public String list(Example example, ModelMap model) {

    example.setOrderKey("EXAMPLE_BOARD_IDX");
    example.setOrderValue("DESC");

    int totalCount = exampleService.getExampleCount(example);

    Pagination pagination = new Pagination(example.getPageNo(), example.getDataPerPage(), totalCount);
    example.setFromNo(pagination.getFromNo());
    example.setDataPerPage(pagination.getDataPerPage());

    List<Example> exampleList = exampleService.getExampleList(example);

    setRowNums(exampleList, totalCount, pagination.getFromNo());

    model.addAttribute("exampleList", exampleList);
    model.addAttribute("totalCount", totalCount);
    model.addAttribute("pagination", pagination);
    return JSP_PATH + "exampleList";
  }

  /**
   * 예제 게시판 입력 폼
   * @return
   */
  @RequestMapping(value = "/new", method = RequestMethod.GET)
  public String newAttachExample() {
    return JSP_PATH + "exampleDetail";
  }

  /**
   * 예제 게시판 입력 프로세스
   * @param example
   * @return
   */
  @RequestMapping(value = "/insert", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> insertExample(@RequestBody Example example) throws Exception {

    exampleService.insertExample(example);

    return createResponseEntity(true);
  }

  /**
   * 예제 게시판 수정 폼
   * @param idx
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/modify/{idx}", method = RequestMethod.POST)
  public String modifyExample(@PathVariable("idx") Long idx, HttpServletResponse response, ModelMap model) throws Exception {
    Example example = exampleService.getExample(idx);

    if(example == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return "redirect:" + JSP_PATH + "exampleList";
    }

    model.addAttribute("example", example);

    return JSP_PATH + "exampleDetail";
  }

  /**
   * 예제 게시판 수정 프로세스
   * @param example
   * @return
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> updateExample(@RequestBody Example example) throws Exception {

    exampleService.updateExample(example);

    return createResponseEntity(true);
  }

  /**
   * 예제 게시판 삭제 프로세스
   * @param example
   * @return
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> deleteExample(@RequestBody Example example) throws Exception {

    exampleService.deleteExample(example);

    return createResponseEntity(true);
  }
}
