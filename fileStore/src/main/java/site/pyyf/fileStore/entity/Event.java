package site.pyyf.fileStore.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Event {

    //topic: 删除
    private String topic;

    //userId 我
    private int userId;

    //entityType: 文件0/文件夹1
    private int entityType;

    // 文件or文件夹的information
    private String entityInfo;

    //附加信息
    private Map<String, Object> data = new HashMap<>();


    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
