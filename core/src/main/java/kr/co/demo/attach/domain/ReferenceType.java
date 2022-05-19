package kr.co.demo.attach.domain;

import kr.co.demo.attach.filter.AttachFilter;
import kr.co.demo.attach.filter.AttachFilterChain;

import java.util.List;

public interface ReferenceType {

    String getTypeName();

    MapCode getMapCode(String mapCode);

    MapCode addMapCode(String mapCode);

    MapCode addMapCode(String mapCode, List<AttachFilter> attachFilterList);

    MapCode addFilter(String mapCode, AttachFilter attachFilter);

    AttachFilterChain getFilterList(String mapCode);

}
