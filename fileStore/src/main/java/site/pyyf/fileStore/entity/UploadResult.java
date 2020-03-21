package site.pyyf.fileStore.entity;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult {

    private String url;
    private String status;
}