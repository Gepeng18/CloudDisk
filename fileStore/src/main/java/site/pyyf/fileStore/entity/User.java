package site.pyyf.fileStore.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (User)用户实体类
 *
 * @author 鹏圆
 * @since 2020-02-25 17:19:04
 */
@AllArgsConstructor
@Data
@Setter
@Getter
@Builder
public class User implements Serializable {
    /**
    * 用户ID
    */
    private Integer id;
    /**
    * 用户的openid
    */
    private String openId;

    /**
    * 用户名
    */
    private String userName;

    /**
    * 注册时间
    */
    private Date registerTime;
    /**
     * 头像地址
     */
    private String imagePath;
    /**
     * 当前容量（单位KB）
     */
    private Integer currentSize;
    /**
     * 最大容量（单位KB）
     */
    private Integer maxSize;

    /**
     * 根文件夹
     */
    private Integer rootFolder;

}