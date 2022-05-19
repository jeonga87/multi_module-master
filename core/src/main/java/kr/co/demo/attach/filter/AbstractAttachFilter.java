package kr.co.demo.attach.filter;

import java.io.File;
import java.nio.file.Paths;

public abstract class AbstractAttachFilter implements AttachFilter {
    public File createTempOutFile(File file) {
        return new File(file.getAbsoluteFile().getParentFile().getAbsolutePath(), "_filter_" + file.getName());
    }

    public void moveTempFileToOriginalFile(File tempFile, File originalFile) {
        String filename = originalFile.getName();
        originalFile.delete();
        tempFile.renameTo(Paths.get(tempFile.getAbsoluteFile().getParentFile().getAbsolutePath(), filename).toFile());
    }

}
