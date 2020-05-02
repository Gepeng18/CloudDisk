package site.pyyf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import site.pyyf.fileStore.FileStoreApplication;
import site.pyyf.fileStore.entity.User;
import site.pyyf.fileStore.service.IUserService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = FileStoreApplication.class)
public class TestReadLocalFile {

    @Test
    public void testReadLocalFile() throws Exception {
        final File[] files = new File("C:\\Users\\FHY-GP\\Desktop\\1").listFiles();
        for(File file:files)
            System.out.println(file.getAbsolutePath());
    }

}
