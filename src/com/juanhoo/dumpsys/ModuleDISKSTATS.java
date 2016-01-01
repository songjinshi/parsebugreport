package com.juanhoo.dumpsys;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.parse.Module;

/**
 * Created by a20023 on 12/29/2015.
 */
public class ModuleDISKSTATS extends Module {
    public ModuleDISKSTATS() {
        moduleName = "DiskStats";
        displayName = "Disk Status Service";
        outputPath = "diskstatus.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }

    String info = "<hr><br><b>DUMP OF SERVICE diskstats</b><hr>";

    @Override
    public void AddLine(String line) {
        super.AddLine(line);
        if (line.contains("Free")) {
            info += "<br>" + line;
        }
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.MEMORYFILEID, info);
        return treeNode;
    }
}
