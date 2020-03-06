package site.pyyf.cloudpan.service.impl;

import org.springframework.stereotype.Service;
import site.pyyf.cloudpan.service.ICompilerService;

@Service
public class CompilerServiceImpl implements ICompilerService {
    @Override
    public StringBuilder addJavaCompileHtml(StringBuilder code, String language) {
        //在<pre><code class="language-java">前加


        int count = 0;
        int index;
        int start = 0;
        String codeToString = code.toString();
        while ((index = codeToString.indexOf("<pre><code class=\"language-" + language + "\">", start)) != -1) {
            code.insert(index, "<button id=\"button"+count+"\" onclick=\"compile('" + count + "')\" class=\"btn btn-success\">\n" +
                    "             <i class=\"fa fa-play\"></i>&nbsp;  运行\n" +
                    "    </button>" +
                    "<div id=\"code" + count + "\">");
            count++;
            int end = code.toString().indexOf("</code></pre>", index) + "</code></pre>".length();
            code.insert(end, "</div>");


            codeToString = code.toString();
            start = end;

        }
        return code;
    }

}
