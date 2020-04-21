package site.pyyf.fileStore.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * (FileFolder)文件夹实体类
 *
 * @author 鹏圆
 * @since 2020-02-25 17:19:04
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Builder
public class FileFolder implements Serializable {

    /**
    * 文件夹ID
    */
    private Integer id;
    /**
    * 文件夹名称
    */
    private String fileFolderName;
    /**
    * 父文件夹ID
    */
    private Integer parentFolderId;
    /**
    * 所属用户ID
    */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

}