package kr.co.demo.attach.policy.impl;

import kr.co.demo.attach.policy.DirectoryPathPolicy;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@Qualifier("dateDirectoryPathPolicy")
public class DateDirectoryPathPolicy implements DirectoryPathPolicy {

  public static final String FILE_SEP = "/";

  public enum DATE_SUBDIR_TYPE {
    YYYY("yyyy"), YYYY_MM("yyyy" + FILE_SEP + "MM"), YYYY_MM_DD("yyyy" + FILE_SEP + "MM" + FILE_SEP + "dd");

    private String format;

    DATE_SUBDIR_TYPE(String format) {
        this.format = format;
    }

    public String getFormat() {
          return format;
      }
  }

  private DATE_SUBDIR_TYPE dateSubDirType;

  private String saveDir = "";
  private String tempDir = "";

  public DateDirectoryPathPolicy() {}

  public DateDirectoryPathPolicy(DATE_SUBDIR_TYPE dateSubDirType, String saveDir, String tempDir) {
    this.dateSubDirType = dateSubDirType;
    if(saveDir.endsWith(FILE_SEP)) {
        saveDir = saveDir.substring(0, saveDir.length() -1);
    }
    this.saveDir = saveDir;

    this.tempDir = tempDir;
  }

  @Override
  public String getTempDir() {
      return this.tempDir;
  }

  @Override
  public String getSaveDir() {
      return this.saveDir;
  }

  @Override
  public String getSubDir() {
    Calendar.getInstance().getTime();

    String dir = DateFormatUtils.format(Calendar.getInstance().getTime(), this.dateSubDirType.getFormat());

    return dir + FILE_SEP;
  }

}
