package site.pyyf.cloudpan.entity;


import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor
public class Directory {
    String contentId;
    String content;
    private LinkedHashMap<String, Directory> subNodes = new LinkedHashMap<>();

    // 添加子节点
    public void addSubNode(String fileName, Directory node) {
        subNodes.put(fileName, node);
    }

    // 获取子节点
    public Directory getSubNode(String fileName) {
        return subNodes.get(fileName);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 获取子节点
    public Map<String, Directory> getAllNode() {
        return subNodes;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String uuid) {
        this.contentId = uuid;
    }
}