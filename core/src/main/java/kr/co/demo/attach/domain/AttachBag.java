package kr.co.demo.attach.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class AttachBag extends HashMap<String, List<Attach>> implements Serializable {
    public Attach one(String mapCode) {
        List<Attach> attachList = get(mapCode);
        if(attachList != null && attachList.size() > 0) {
            return attachList.get(0);
        }
        return null;
    }
}
