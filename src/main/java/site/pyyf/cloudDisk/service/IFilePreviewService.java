package site.pyyf.cloudDisk.service;

import java.util.List;

public interface IFilePreviewService {
    public String addQuotationMarks(String language, StringBuilder oriCode);

    public StringBuilder addHtmlShowStyle(StringBuilder code, List<String> languages);

    public StringBuilder addHtmlCompileModule(StringBuilder code, String language);

}
