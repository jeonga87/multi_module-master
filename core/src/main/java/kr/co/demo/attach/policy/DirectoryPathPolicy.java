package kr.co.demo.attach.policy;

public interface DirectoryPathPolicy {
    String getTempDir();
    String getSaveDir();
    String getSubDir();
}
