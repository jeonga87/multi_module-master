package kr.co.demo.attach.domain;

import kr.co.demo.attach.filter.impl.MaxSizeExceptionFilter;
import kr.co.demo.attach.filter.impl.ResizeImageFilter;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 첨부파일용 ReferenceType 설정
 */
@Getter
public class ReferenceTypeRegistry implements Serializable {
    private static Map<String, ReferenceType> refTypeMap = new HashMap<>();

    public static ReferenceType get(String type) {
        return refTypeMap.get(type);
    }

    /** 에디터 */
    public static ReferenceType EDITOR = new DefaultReferenceType() {
        @Override
        public String getTypeName() {
            return "editor";
        }
    };
    static {
        EDITOR.addMapCode("default");
        refTypeMap.put("EDITOR", EDITOR);
    }

    /** (개발참고용) 예제 게시판 */
    public static ReferenceType EXAMPLE = new DefaultReferenceType() {
        @Override
        public String getTypeName() {
            return "example";
        }
    };
    static {
        try {
            EXAMPLE.addMapCode("thumb")   // 썸네일 이미지
                    // 가로크기 제한
                    .addFilter(
                            ResizeImageFilter.class,
                            ImmutableMap.<String, Object>of(ResizeImageFilter.PARAM_MAX_WITH, 500)
                    )
                    // 세로크기 제한
                    .addFilter(
                            ResizeImageFilter.class,
                            ImmutableMap.<String, Object>of(ResizeImageFilter.PARAM_MAX_HEIGHT, 500)
                    )
                    // 용량제한
                    .addFilter(
                            MaxSizeExceptionFilter.class,
                            ImmutableMap.<String, Object>of(MaxSizeExceptionFilter.PARAM_MAX_SIZE, 1048576l)
                    );
            EXAMPLE.addMapCode("image");   // 대표 이미지
            EXAMPLE.addMapCode("attach");   // 첨부파일
        } catch (Exception e) {
            e.printStackTrace();
        }
        refTypeMap.put("EXAMPLE", EXAMPLE);
    }
}
