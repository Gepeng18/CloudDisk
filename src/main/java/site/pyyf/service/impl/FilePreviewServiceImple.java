package site.pyyf.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import site.pyyf.service.IfilePreviewService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilePreviewServiceImple implements IfilePreviewService {
    @Override
    public String preview(String language, StringBuilder oriCode) {

        StringBuilder suffixAndCode = new StringBuilder();
        suffixAndCode.append("```").append(language).append("\n").append(oriCode).append("```");
        return suffixAndCode.toString();

    }
}
