package site.pyyf.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ebook {
    private int id;
    private String ebookName;
    private int ebookId;
    private String header;
}
