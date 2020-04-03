package site.pyyf.fileStore.service;

import java.util.List;

public interface ICodePreviewService {
    String addQuotationMarks(String language, StringBuilder oriCode);

    StringBuilder addHtmlShowStyle(StringBuilder code, List<String> languages);

    StringBuilder addHtmlCompileModule(StringBuilder code, String language);

}
