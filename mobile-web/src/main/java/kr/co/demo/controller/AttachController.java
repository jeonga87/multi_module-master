package kr.co.demo.controller;

import kr.co.demo.attach.domain.Attach;
import kr.co.demo.attach.domain.AttachBag;
import kr.co.demo.attach.domain.ReferenceTypeRegistry;
import kr.co.demo.attach.policy.DirectoryPathPolicy;
import kr.co.demo.attach.service.AttachService;
import kr.co.demo.config.properties.FileUploadProperties;
import kr.co.demo.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Class Name : AttachController.java
 * Description : 첨부파일 컨트롤러 클래스
 * Modification Information
 * Generated : Source Builder
 */

@RestController
public class AttachController extends BaseFormController {

  private final String URI_PREFIX = "/api/attach";

  private final Logger log = LoggerFactory.getLogger(AttachController.class);

  private static final String FILE_SEP = "/";

  @Inject
  private AttachService attachService;

  @Inject
  private FileUploadProperties fileUploadProperties;

  @Autowired
  @Qualifier("dateDirectoryPathPolicy")
  private DirectoryPathPolicy directoryPathPolicy;

  /**
   * GET  /{refType}/{refKey} : 첨부파일 조회
   * @param refType   참조타입코드
   * @param refKey    참조키
   * @return 첨부파일 객체
   */
  @RequestMapping(value = URI_PREFIX + "/{refType}/{refKey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AttachBag> getAttach(@PathVariable("refType") String refType, @PathVariable("refKey") String refKey) {
    AttachBag attachBag = attachService.getAttachBagByRef(refType, refKey);
    return createResponseEntity(attachBag != null, attachBag);
  }

  /**
   * GET /view/{attachNo} : 이미지 보기
   * @param attachNo 첨부파일 번호
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/view/{attachNo}", method = RequestMethod.GET)
  public void view(@PathVariable("attachNo") Long attachNo, HttpServletRequest request, HttpServletResponse response) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    if (!WebUtil.needFreshResponse(request, dateFormat)) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    Attach attach = attachService.getAttach(attachNo);

    if(attach == null) {
      return;
    }

    File f = new File(fileUploadProperties.getSaveDir() + attach.getSavedDir() + attach.getSavedName());

    if(!f.exists()) {
        return;
    }

    FileInputStream fin = null;
    FileChannel inputChannel = null;
    WritableByteChannel outputChannel = null;

    WebUtil.setCacheHeader(response, dateFormat);

    response.setContentType(attach.getFileType());
    response.setContentLength( (int)f.length() );

    WebUtil.writeFile(response, f, fin, inputChannel, outputChannel);
  }

  /**
   * GET /view/{refType}/{refKey}/{mapCode} : 이미지 보기
   *
   * @param refType   참조타입코드 - 예) 상품 = product
   * @param refKey    참조키 - 예) 상품 아이디 : 271
   * @param mapCode   세부타입코드 - 예) 섬네일 이미지 : thumbnail
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/view/{refType}/{refKey}/{mapCode}", method = RequestMethod.GET)
  public void viewByRefType(
          @PathVariable("refType") String refType,
          @PathVariable("refKey") String refKey,
          @PathVariable("mapCode") String mapCode,
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {

      viewByRefTypeOrder(refType, refKey, mapCode, 1, request, response);
  }

  /**
   * GET /view/{refType}/{refKey}/{mapCode}/{idx} : 이미지 보기
   *
   * @param refType   참조타입코드 - 예) 상품 = product
   * @param refKey    참조키 - 예) 상품 아이디 : 271
   * @param mapCode   세부타입코드 - 예) 섬네일 이미지 : thumbnail
   * @param idx       획득하고자 하는 이미지 순서 인덱스 : "1" 로 시작
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/view/{refType}/{refKey}/{mapCode}/{idx}", method = RequestMethod.GET)
  public void viewByRefTypeOrder(
          @PathVariable("refType") String refType,
          @PathVariable("refKey") String refKey,
          @PathVariable("mapCode") String mapCode,
          @PathVariable("idx") Integer idx,
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
      SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

      if (!WebUtil.needFreshResponse(request, dateFormat)) {
          response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
          return;
      }

      List<Attach> attachList = attachService.listAttachByRef(refType, refKey, mapCode);

      if(CollectionUtils.isEmpty(attachList) || attachList.size() < idx) {
          return;
      }

      Attach attach = attachList.get(idx - 1);

      File f = new File(fileUploadProperties.getSaveDir() + attach.getSavedDir() + attach.getSavedName());

      if(!f.exists()) {
          return;
      }

      FileInputStream fin = null;
      FileChannel inputChannel = null;
      WritableByteChannel outputChannel = null;

      WebUtil.setCacheHeader(response, dateFormat);

      response.setContentType(attach.getFileType());
      response.setContentLength( (int)f.length() );

      WebUtil.writeFile(response, f, fin, inputChannel, outputChannel);
  }

  /**
   * GET /download/{refType}/{refKey}/{mapCode} : 파일 다운로드
   *
   * @param refType   참조타입코드 - 예) 상품 = product
   * @param refKey    참조키 - 예) 상품 아이디 : 271
   * @param mapCode   세부타입코드 - 예) 섬네일 이미지 : thumbnail
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/download/{refType}/{refKey}/{mapCode}", method = RequestMethod.GET)
  public void downloadByRefType(
          @PathVariable("refType") String refType,
          @PathVariable("refKey") String refKey,
          @PathVariable("mapCode") String mapCode,
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {

      downloadByRefTypeOrder(refType, refKey, mapCode, 1, request, response);
  }

  /**
   * GET /download/{refType}/{refKey}/{mapCode}/{idx} : 파일 다운로드
   *
   * @param refType   참조타입코드 - 예) 상품 = product
   * @param refKey    참조키 - 예) 상품 아이디 : 271
   * @param mapCode   세부타입코드 - 예) 섬네일 이미지 : thumbnail
   * @param idx       획득하고자 하는 이미지 순서 인덱스 : "1" 로 시작
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/download/{refType}/{refKey}/{mapCode}/{idx}", method = RequestMethod.GET)
  public void downloadByRefTypeOrder(
          @PathVariable("refType") String refType,
          @PathVariable("refKey") String refKey,
          @PathVariable("mapCode") String mapCode,
          @PathVariable("idx") Integer idx,
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
      SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

      if (!WebUtil.needFreshResponse(request, dateFormat)) {
          response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
          return;
      }

      if(attachService.checkViewAuth(refType, refKey, mapCode, idx) == false) {
          return;
      }

      List<Attach> attachList = attachService.listAttachByRefWithCache(refType, refKey, mapCode);

      if(CollectionUtils.isEmpty(attachList) || attachList.size() < idx) {
          return;
      }

      Attach attach = attachList.get(idx - 1);

      File f = new File(fileUploadProperties.getSaveDir() + attach.getSavedDir() + attach.getSavedName());

      if(!f.exists()) {
          return;
      }

      FileInputStream fin = null;
      FileChannel inputChannel = null;
      WritableByteChannel outputChannel = null;

      response.setContentType(attach.getFileType());
      response.setContentLength( (int)f.length() );
      response.setHeader( "Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(attach.getDisplayName(), "UTF-8") + "\"" );

      WebUtil.writeFile(response, f, fin, inputChannel, outputChannel);
  }

  /**
   * GET /download/{attachNo} : 파일 다운로드
   * @param attachNo 첨부파일 아이디
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/download/{attachNo}", method = RequestMethod.GET)
  public void download(@PathVariable("attachNo") Long attachNo, HttpServletResponse response) throws Exception {
    Attach attach = attachService.getAttach(attachNo);

    if(attach == null) {
      return;
    }

    File f = new File(fileUploadProperties.getSaveDir() + attach.getSavedDir() + attach.getSavedName());

    if(!f.exists()) {
      return;
    }

    FileInputStream fin = null;
    FileChannel inputChannel = null;
    WritableByteChannel outputChannel = null;

    response.setContentType(attach.getFileType());
    response.setContentLength( (int)f.length() );
    response.setHeader( "Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(attach.getDisplayName(), "UTF-8") + "\"" );

    WebUtil.writeFile(response, f, fin, inputChannel, outputChannel);
  }

  /**
   * GET /editor/view/** : editor 이미지 보기
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/editor/view/**", method = RequestMethod.GET)
  public void editorImageView(HttpServletRequest request, HttpServletResponse response) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    if (!WebUtil.needFreshResponse(request, dateFormat)) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    String filePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    filePath = StringUtils.substringAfter(filePath, URI_PREFIX + "/editor/view");
    String baseDir = directoryPathPolicy.getSaveDir() + FILE_SEP + ReferenceTypeRegistry.EDITOR.getTypeName();

    File f = new File(baseDir + filePath);

    if(!f.exists()) {
      return;
    }

    FileInputStream fin = null;
    FileChannel inputChannel = null;
    WritableByteChannel outputChannel = null;

    WebUtil.setCacheHeader(response, dateFormat);

    response.setContentType(AttachService.detectFile(f));
    response.setContentLength( (int)f.length() );

    WebUtil.writeFile(response, f, fin, inputChannel, outputChannel);
  }

  /**
   * GET /temp/view/** : 임시 업로드 이미지 보기
   *
   * @param request
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = URI_PREFIX + "/temp/view/**",
          method      = RequestMethod.GET)
  public void tempImageView(HttpServletRequest request, HttpServletResponse response) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    if (!WebUtil.needFreshResponse(request, dateFormat)) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    String filePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    filePath = StringUtils.substringAfter(filePath, URI_PREFIX + "/temp/view");
    String baseDir = directoryPathPolicy.getTempDir();

    File f = new File(baseDir + filePath);

    if(!f.exists()) {
      return;
    }

    FileInputStream fin = null;
    FileChannel inputChannel = null;
    WritableByteChannel outputChannel = null;

    WebUtil.setCacheHeader(response, dateFormat);

    response.setContentType(AttachService.detectFile(f));
    response.setContentLength( (int)f.length() );

    WebUtil.writeFile(response, f, fin, inputChannel, outputChannel);
  }
}
