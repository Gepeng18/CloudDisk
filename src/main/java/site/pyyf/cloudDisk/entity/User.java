package site.pyyf.cloudDisk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
@Builder
public class User implements Serializable {
    /**
    * 用户ID
    */
    private Integer userId;
    /**
    * 用户的openid
    */
    private String openId;

    /**
    * 用户名
    */
    private String userName;
    /**
     * 头像地址
     */
    private String imagePath;
    /**
    * 注册时间
    */
    private Date registerTime;
    /**
     * 当前容量（单位KB）
     */
    private Integer currentSize;
    /**
     * 最大容量（单位KB）
     */
    private Integer maxSize;

}