package site.pyyf.cloudpan.service.impl;

import org.springframework.stereotype.Service;
import site.pyyf.cloudpan.service.IfilePreviewService;

@Service
public class FilePreviewServiceImple implements IfilePreviewService {
    @Override
    public String preview(String language, StringBuilder oriCode) {

        StringBuilder suffixAndCode = new StringBuilder();
        suffixAndCode.append("```").append(language).append("\n").append(oriCode).append("```");
        return suffixAndCode.toString();

    }
}
