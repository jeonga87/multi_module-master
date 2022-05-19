package kr.co.demo.controller;

import kr.co.demo.attach.domain.ReferenceTypeRegistry;
import kr.co.demo.attach.filter.AttachFilterChain;
import kr.co.demo.attach.service.AttachService;
import kr.co.demo.config.properties.FileUploadProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name : AttachUploadController.java
 * Description : 첨부파일 업로드 컨트롤러 클래스
 * Modification Information
 * Generated : Source Builder
 */

@RestController
public class AttachUploadController extends BaseFormController {

    private final String URI_PREFIX = "/api/attach";

    private final Logger log = LoggerFactory.getLogger(AttachUploadController.class);

    @Inject
    private AttachService attachService;

    @Inject
    private FileUploadProperties fileUploadProperties;

    @Inject
    private ObjectMapper objectMapper;

    /**
     * POST /tmp : 파일 임시 업로드
     * @param refType   참조타입코드 - 예) 상품 = product
     * @param mapCode   세부타입코드 - 예) 섬네일 이미지 : thumbnail
     * @param mapFilters
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = URI_PREFIX + "/tmp",
            method      = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam(value = "refType") String refType,
            @RequestParam(value = "mapCode") String mapCode,
            @RequestParam(value = "mapFilters", required = false) String mapFilters,
            @RequestParam(value = "file") MultipartFile file) throws Exception {

        AttachFilterChain filterChain = new AttachFilterChain();

        if(mapFilters != null) {
            Map<String, Map<String, Object>> filterMap = objectMapper.readValue(mapFilters, new TypeReference<Map<String, Map<String, Object>>>() {});
            int idx = 0;
            for( String key : filterMap.keySet() ){
                try {
                    Class filterClass = Class.forName(fileUploadProperties.getFilterMap().get(idx));
                    filterChain.addFilter(filterClass, filterMap.get(key));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                idx++;
            }
        }

        String filename = attachService.uploadTemp(file, ReferenceTypeRegistry.get(refType.toUpperCase()), mapCode, filterChain);

        boolean isSuccess = StringUtils.isNotEmpty(filename);

        Map<String, Object> resultMap = new HashMap<>();
        if(isSuccess) {
            resultMap.put("savedName", filename);
        }

        return createResponseEntity(isSuccess, resultMap);
    }

    /**
     * POST /editor : editor 파일 업로드
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = URI_PREFIX + "/editor/upload",
            method      = RequestMethod.POST)
    public Map<String, Object> uploadNoDir(MultipartFile file) throws Exception {
        return upload(file, null);
    }

    /**
     * POST /editor/{dirPath} : editor 파일 업로드 (디렉토리 경로 포함)
     * @param file
     * @param dirPath
     * @return
     * @throws Exception
     */
    @RequestMapping(value = URI_PREFIX + "/editor/upload/{dirPath}",
            method      = RequestMethod.POST)
    public Map<String, Object> upload(
            MultipartFile file,
            @PathVariable("dirPath") String dirPath) throws Exception {

        String filename = attachService.uploadEditorImage(file, dirPath);

        Map<String, Object> resultMap = new HashMap<>();

        String resultText = "";

        resultText += "&sFileName=" + file.getName();
        resultText += "&sFileURL=" + URI_PREFIX + "/editor/view" + filename;
        resultText += "&bNewLine=true";

        resultMap.put("resultText", resultText);

        return resultMap;
    }

}
