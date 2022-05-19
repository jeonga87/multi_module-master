package kr.co.demo.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@EqualsAndHashCode(callSuper=false)
@ConfigurationProperties(prefix = "demo.file-upload")
public class FileUploadProperties {

    private String tempDir;
    private String saveDir;

    private Map<String, String> filterMap;
}
