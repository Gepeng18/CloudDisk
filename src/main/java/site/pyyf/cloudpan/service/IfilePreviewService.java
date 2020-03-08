package site.pyyf.cloudpan.service;

import java.util.List;

public interface IfilePreviewService {
    public String addQuotationMarks(String language, StringBuilder oriCode);

    public StringBuilder addHtmlShowStyle(StringBuilder code, List<String> languages);

    public StringBuilder addHtmlCompileModule(StringBuilder code, String language);

}
