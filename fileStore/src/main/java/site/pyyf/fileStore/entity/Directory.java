package site.pyyf.fileStore.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
public class Directory {
    private String contentId;
    private String header;
    private LinkedList<Directory> subNodes = new LinkedList<>();

    // 添加子节点
    public void addSubNode(Directory node) {
        subNodes.add(node);
    }

}