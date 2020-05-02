package site.pyyf.fileStore.entity;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EbookContent {
    private Integer id;
    private String contentId;
    private String content;
    private Integer fileId;
}
