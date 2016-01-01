package com.juanhoo.dumpservice;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.parse.Module;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/29/2015.
 */
public class ModuleGcmService  extends Module {
    public ModuleGcmService() {
        moduleName = "GcmService";
        displayName = "GCM Service";
        outputPath = "GcmService.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }

    private String info = "<hr><br><b>GCM Service Connection log</b><hr>";
    //    22:06:57 1: Received com.google.android.gms time=36
    private String pattern = "\\s*?([\\d]{2}:[\\d]{2}:[\\d]{2}) (.*?)";

    @Override

    public void AddLine(String line) {
        super.AddLine(line);
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            info += "<br>" + line;
        }
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.NETWORKFILEID, info);
        return super.Parse();
    }
}
