package site.pyyf.compiler;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableConfigurationProperties
@EnableDubbo(scanBasePackages="site.pyyf.compiler")
@EnableHystrix
@SpringBootApplication
public class CompilerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompilerApplication.class, args);
    }

}
