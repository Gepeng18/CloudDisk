package site.pyyf.fileStore.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ebook {
    private int id;
    private String ebookName;
    private int fileId;
    private String header;
}
