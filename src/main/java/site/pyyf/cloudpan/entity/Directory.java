package site.pyyf.cloudpan.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Directory {
    String contentId;
    String header;
    private LinkedList<Directory> subNodes = new LinkedList<>();

    // 添加子节点
    public void addSubNode(Directory node) {
        subNodes.add(node);
    }

}