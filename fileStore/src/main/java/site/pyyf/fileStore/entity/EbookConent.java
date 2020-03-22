package site.pyyf.fileStore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EbookConent {
    private int id;
    private String contentId;
    private String content;
    private int fileId;
}
