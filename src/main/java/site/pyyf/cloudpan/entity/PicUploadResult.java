package site.pyyf.cloudpan.entity;

import lombok.Data;

@Data
public class PicUploadResult {

    private String name;
    private int id;
    private Integer error;
    private String originalFilename;
    private String url;
    private String Message;
    private String status;





}