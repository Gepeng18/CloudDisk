package site.pyyf.cloudDisk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "clouddisk")
@Data
public class CloudDiskConfig {
    private int maxShowSize;
    private String type;
}
