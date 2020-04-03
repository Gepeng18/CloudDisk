package site.pyyf.fileStore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ebook {
    private int ebookId;
    private String ebookName;
    private int fileId;
    private String header;
}
