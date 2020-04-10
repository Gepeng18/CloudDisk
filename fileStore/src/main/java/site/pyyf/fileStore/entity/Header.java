package site.pyyf.fileStore.entity;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Header {
    private String contentId; //内容ID号
    private String header; //标题
    private boolean hasSub = false;
    private LinkedList<Header> subNodes = new LinkedList<>();

    // 添加子节点
    public void addSubNode(Header node) {
        subNodes.add(node);
    }

}