package kr.co.demo.attach.filter;

import org.apache.tika.Tika;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttachFilterChain {

    private Tika tika = new Tika();

    private String contentType = null;

    public AttachFilterChain() {

    }

    private List<AttachFilter> filterList = new ArrayList<>();

    private Iterator<AttachFilter> iterator;

    public void addFilter(Class<? extends AttachFilter> klass, Map<String, Object> config) throws Exception {
        AttachFilter filter = klass.newInstance();
        filter.config(config);
        filterList.add(filter);
    }

    public int size() {
        return filterList.size();
    }

    public void startFilter(File file) throws Exception {
        detectFile(file);
        iterator = filterList.iterator();
        doFilter(file);
    }

    public void doFilter(File file) throws Exception {
        if(iterator.hasNext()) {
            iterator.next().doFilter(this, file);
        }
    }

    /**
     * 이미지 구분
     * @param file
     * @return
     * @throws Exception
     */
    private String detectFile(File file) throws Exception {
        if(contentType == null) {
            try {
                synchronized (tika) {
                    contentType = tika.detect(file);
                }
            } catch (Exception e) {
                contentType = new MimetypesFileTypeMap().getContentType(file);
            }
        }
        return contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public static void main(String[] args) throws Exception {
        File file = Paths.get("/Users/guava/Downloads/", "Wallpaper-16.jpg").toFile();
        AttachFilterChain chain = new AttachFilterChain();
        System.out.println(chain.detectFile(file));
    }
}
