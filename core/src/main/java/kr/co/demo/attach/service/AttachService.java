package kr.co.demo.attach.service;

import kr.co.demo.attach.domain.*;
import kr.co.demo.attach.filter.AttachFilterChain;
import kr.co.demo.attach.policy.DirectoryPathPolicy;
import kr.co.demo.attach.repository.AttachRepository;
import kr.co.demo.config.CacheConfiguration;
import kr.co.demo.security.SecurityUtils;
import kr.co.demo.util.WebUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class Name : AttachService.java
 * Description : 첨부파일 서비스 클래스
 * Modification Information
 * Generated : Source Builder
 */

@Service
public class AttachService {

    private final Logger log = LoggerFactory.getLogger(AttachService.class);

    public final static String DELETED_MAP_CODE = "_deleted";

    public static final String FILE_SEP = "/";

    @Autowired
    @Qualifier("dateDirectoryPathPolicy")
    private DirectoryPathPolicy directoryPathPolicy;

    @Inject
    private AttachRepository attachRepository;

    @Autowired
    private Environment env;

    private static Tika tika = new Tika();

    public String uploadTemp(MultipartFile file, ReferenceType refType, String mapCode, AttachFilterChain filterChain) throws Exception {

        createDirectoryIfNotExists(directoryPathPolicy.getTempDir());

        if (!file.isEmpty()) {
            try {
                String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
                String fileName = UUID.randomUUID().toString() + "." + extension;
                Path path = Paths.get(directoryPathPolicy.getTempDir(), fileName);
                Files.copy(file.getInputStream(), path);

                try {
                    if(filterChain == null || filterChain.size() == 0) {
                        MapCode map = refType.getMapCode(mapCode);
                        if(map == null) {
                            throw new Exception("Unsupported mapCode : " + mapCode);
                        }
                        refType.getMapCode(mapCode).startFilter(path.toFile());
                    }else {
                        filterChain.startFilter(path.toFile());
                    }
                }catch(Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e.getMessage());
                }

                return fileName;
            } catch (IOException |RuntimeException e) {
                throw new Exception("Failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            throw new FileNotFoundException("Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }
    }

    @Transactional
    public void save(ReferenceType refType, String refKey, Map<String, List<Attach>> bag) throws Exception {
        if(bag == null || bag.isEmpty()) {
            return;
        }

        // 실제 첨부파일이 저장되는 경로 (기본경로/기능별경로/날짜/)
        String saveDir = directoryPathPolicy.getSaveDir() + FILE_SEP + refType.getTypeName() + FILE_SEP + directoryPathPolicy.getSubDir();

        createDirectoryIfNotExists(saveDir);

        for(Map.Entry<String, List<Attach>> entry: bag.entrySet()) {
            String mapCode = entry.getKey();
            if(StringUtils.equals(mapCode, DELETED_MAP_CODE)) {
                deleteAttachPermanently(entry.getValue());
            }else {
                int changeCnt = 0;  // 파일 변경 횟수
                for(Attach attach : entry.getValue()) {
                    attach.setAltValue(attach.getAltValue());
                    if(attach.getIdx() != null) {
                        // 첨부파일 수정(설명만 변경)
                        attachRepository.updateAttach(attach);
                    } else {
                        // 첨부파일 등록
                        String extension = StringUtils.substringAfterLast(attach.getDisplayName(), ".");

                        InputStream is = Files.newInputStream(Paths.get(directoryPathPolicy.getTempDir(), attach.getSavedName()));

                        String fileName = (StringUtils.isNotBlank(mapCode) ? mapCode + "_" : "")
                                + RandomStringUtils.randomAlphanumeric(20)
                                + "."
                                + extension;

                        String uniqueName = getUniqueFileName(saveDir, fileName);
                        Files.copy(is, Paths.get(saveDir, uniqueName));
                        attach.setSavedName(uniqueName);
                        attach.setRefType(refType.getTypeName());
                        attach.setRefKey(refKey);
                        attach.setSavedDir(StringUtils.removeStart(saveDir, directoryPathPolicy.getSaveDir()));
                        attach.setRefMapCode(mapCode);
                        attachRepository.insertAttach(attach);
                        changeCnt++;
                    }
                }

                // 첨부파일 변경이 일어났으면 캐시 초기화
                if(changeCnt > 0) {
                    String cachePath = "/cache/reset/attach/view/" + refType.getTypeName() + "/" + refKey + "/" + mapCode;
                    clearCache(refType.getTypeName(), refKey, mapCode);
                    WebUtil.postUrl(env.getProperty("demo.url.pc-web") + cachePath);      // PC-WEB
                    WebUtil.postUrl(env.getProperty("demo.url.mobile-web") + cachePath);  // MOBILE-WEB
                }
            }
        }
    }

    @Transactional
    @CacheEvict(CacheConfiguration.ATTACH_IMAGE_VIEW)
    public void clearCache(String refType, String refKey, String mapCode) {
        log.debug("캐시 초기화. refType: " + refType + ", refKey: " + refKey + ", mapCode: " + mapCode);
    }

    @Transactional
    public void copy(ReferenceType refType, String refKey, String newRefKey, AttachBag bag) throws Exception {
        // 실제 첨부파일이 저장되는 경로 (기본경로/기능별경로/날짜/)
        String saveDir = directoryPathPolicy.getSaveDir() + FILE_SEP + refType.getTypeName() + FILE_SEP + directoryPathPolicy.getSubDir();

        createDirectoryIfNotExists(saveDir);

        Map<String, Object> param = new HashMap<>();
        param.put("refType", refType.getTypeName());
        param.put("refKey", refKey);
        List<Attach> attachList = attachRepository.listAttach(param);

        List<Attach> deletedList = bag.get(DELETED_MAP_CODE);

        List<Long> deletedIdList = new ArrayList();
        if(deletedList != null) {
            for(Attach deletedItem : deletedList) {
                if(deletedItem.getIdx() != null) {
                    deletedIdList.add(deletedItem.getIdx());
                }
            }
        }

        bag.remove(DELETED_MAP_CODE);

        for(Attach attach : attachList) {
            if(deletedIdList.contains(attach.getIdx())) {
                continue;
            }
            try {
                InputStream is = Files.newInputStream(Paths.get(directoryPathPolicy.getSaveDir() + attach.getSavedDir(), attach.getSavedName()));
                String mapCode = attach.getRefMapCode();
                String extension = StringUtils.substringAfterLast(attach.getSavedName(), ".");
                String fileName = (StringUtils.isNotBlank(mapCode) ? mapCode + "_" : "")
                        + RandomStringUtils.randomAlphanumeric(20)
                        + "."
                        + extension;

                String uniqueName = getUniqueFileName(saveDir, fileName);
                Files.copy(is, Paths.get(saveDir, uniqueName));
                attach.setIdx(null);
                attach.setSavedName(uniqueName);
                attach.setSavedDir(StringUtils.removeStart(saveDir, directoryPathPolicy.getSaveDir()));
                attach.setRefKey(newRefKey);
                attachRepository.insertAttach(attach);
            }catch(Exception e) {
                log.error(e.getMessage(), e);
            }

        }

    }

    public String uploadEditorImage(MultipartFile upload, String dirPath) throws Exception {
        // 실제 에디터 첨부파일이 저장되는 경로 (기본경로/기능별경로/날짜/)
        String saveDir = directoryPathPolicy.getSaveDir() + FILE_SEP + ReferenceTypeRegistry.EDITOR.getTypeName();

        if(dirPath == null) {
            dirPath = "";
        }else {
            dirPath = FILE_SEP + dirPath;
        }

        String subDir = dirPath + FILE_SEP + directoryPathPolicy.getSubDir();

        String sdir = saveDir + subDir;

        if(upload != null && upload.getSize() != 0) {

            File dir = new File(sdir);
            if(!dir.exists()) { // 경로 존재하지 않으면 강제로 만들어줌
                dir.mkdirs();
            }
            String orgName = upload.getOriginalFilename();

            String extension = StringUtils.substringAfterLast(orgName, ".");

            if(StringUtils.isEmpty(extension)) {
                extension = getFormatName(detectFile(upload.getInputStream()));
            }

            String fileName = RandomStringUtils.randomAlphanumeric(20) + "." + extension;

            String uniqueName = getUniqueFileName(sdir, fileName);

            Files.copy(upload.getInputStream(), Paths.get(sdir, uniqueName));
            return subDir + uniqueName;
        }
        return null;
    }

    private void deleteAttachPermanently(List<Attach> attaches) {
        for(Attach attach : attaches) {
            Attach savedAttach = attachRepository.getAttach(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("idx", attach.getIdx()).build()));
            if(savedAttach != null && StringUtils.isNoneBlank(savedAttach.getSavedDir()) && StringUtils.isNoneBlank(savedAttach.getSavedName())) {
                File t = new File(savedAttach.getSavedDir()+savedAttach.getSavedName());
                if(t.exists()) {
                    t.delete();
                }
                attachRepository.deleteAttach(attach.getIdx());
            } else {
                throw new InvalidPathException("", "No path specified");
            }
        }
    }

    private void createDirectoryIfNotExists(String saveDir) throws IOException {
        Path path = Paths.get(saveDir);
        //if directory exists?
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * 첨부파일 상세 조회
     * @param idx
     * @return Attach
     */
    @Transactional(readOnly = true)
    public Attach getAttach(Long idx) {
        return attachRepository.getAttach(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("idx", idx).build()));
    }

    /**
     * 첨부파일 카운트 조회
     * @param param
     * @return
     */
    @Transactional(readOnly = true)
    public int listAttachCnt(Map<String, Object> param) {
        return attachRepository.listAttachCnt(param);
    }

    @Transactional(readOnly = true)
    public int listAttachCntByRef(String refType, String refId, String mapCode) throws IOException {
        Map<String, Object> param = new HashMap();
        param.put("refType", refType);
        param.put("refKey", refId);
        param.put("refMapCode", mapCode);
        return attachRepository.listAttachCnt(param);
    }

    public List<Attach> listAttachByRef(String refType, String refId, String mapCode) {
        Map<String, Object> param = new HashMap();
        param.put("refType", refType);
        param.put("refKey", refId);
        param.put("refMapCode", mapCode);
        List<Attach> list = attachRepository.listAttach(param);
        return list;
    }

    /**
     * 첨부파일 목록 조회 - 캐시 이용
     * @param refType
     * @param refKey
     * @param mapCode
     * @return
     */
    @Cacheable(CacheConfiguration.ATTACH_IMAGE_VIEW)
    public List<Attach> listAttachByRefWithCache(String refType, String refKey, String mapCode) {
        return this.listAttachByRef(refType, refKey, mapCode);
    }

    public AttachBag getAttachBagByRef(String refType, String refId) {
        Map<String, Object> param = new HashMap();
        param.put("refType", refType);
        param.put("refKey", refId);
        List<Attach> list = attachRepository.listAttach(param);

        AttachBag bag = new AttachBag();

        for(Attach attach : list) {
            if(!bag.containsKey(attach.getRefMapCode())) {
                bag.put(attach.getRefMapCode(), new ArrayList<Attach>());
            }
            bag.get(attach.getRefMapCode()).add(attach);
        }

        return bag;
    }


    private String getUniqueFileName(String path, String fileName) {
        File tmpFile = new File(path + fileName);
        File parentDir = tmpFile.getParentFile();
        int count = 1;
        String extension = FilenameUtils.getExtension(tmpFile.getName());
        String baseName = FilenameUtils.getBaseName(tmpFile.getName());
        String uniqueName = baseName + "_" + count++ + "_." + extension;
        while(tmpFile.exists()) {
            tmpFile = new File(parentDir, uniqueName);
        }
        return uniqueName;
    }

    /**
     * 이미지 구분
     * @param stream
     * @return
     * @throws Exception
     */
    public static String detectFile(InputStream stream) throws Exception {

        String contentType = null;
        try {
            synchronized (tika) {
                contentType = tika.detect(stream);
            }
        }catch (Exception ignore){}
        return contentType;
    }


    public static String detectFile(File file) throws Exception {

        String contentType = null;
        try {
            synchronized (tika) {
                contentType = tika.detect(file);
            }
        }catch (Exception ignore){}
        return contentType;
    }

    private String getFormatName(String contentType) {
        if(StringUtils.indexOf(contentType, "jpeg") >= 0) {
            return "jpg";
        }else if(StringUtils.indexOf(contentType, "jpg") >= 0) {
            return "jpg";
        }else if(StringUtils.indexOf(contentType, "gif") >= 0) {
            return "gif";
        }else if(StringUtils.indexOf(contentType, "png") >= 0) {
            return "png";
        }
        return "jpg";
    }

    /**
     * (사용자) 업로드 가능한 첨부파일인지 확인
     */
    public boolean checkUploadAuth(String refType) {
        boolean isValid = false;
        if(SecurityUtils.isAdmin()) {
            // 관리자는 모두 가능
            isValid = true;
        }
        return isValid;
    }

    /**
     * 업로드 가능한 첨부파일인지 확인
     */
    public boolean checkUploadAuth(String refType, String mapCode) {
        boolean isValid = false;
        if(SecurityUtils.isAdmin()) {
            // 관리자는 모두 가능
            isValid = true;
        }
        return isValid;
    }

    /**
     * (사용자) 조회 권한이 있는 첨부파일인지 확인
     */
    public boolean checkViewAuth(String refType, String refId, String mapCode, Integer idx) {
        boolean isValid = false;
        if(SecurityUtils.isAdmin()) {
            // 관리자는 모두 가능
            isValid = true;
        } else {
            //사용자일 경우 설정가능
            isValid = true;
        }
        return isValid;
    }
}
