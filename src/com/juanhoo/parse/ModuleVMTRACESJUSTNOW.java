package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;

/**
 * Created by a20023 on 11/23/2015.
 */
public class ModuleVMTRACESJUSTNOW extends  Module{
    public ModuleVMTRACESJUSTNOW() {
        displayName = "Trace Stack";
        outputPath ="tracestack.html";
        moduleName = "VMTRACE";
    }

    private String divline1 = "----- pid ";
    private String divline2 = "Cmd line:";
    private Module module = null;
    private boolean isNewDiv = false;
    private boolean DEBUG = false;

    ParseTreeNode childNode = null;
    private String processName = null;


    public void AddLine (String line) {
/*        if (line.length() == 0) {
            return;
        }

        if (isNewDiv == true && line.contains(divline1)!= false) {

            if (module != null) {
                childNode = module.Parse();
                treeNode.childArray.add(childNode);
                processName = null;
                module = null;
            }
        }

        if (processName == null && line.contains(divline2) != false) {
            processName = ConvertSubParseName(line);
            module = new ModuleDefaultHandler(processName, processName.replace('/','_'));
        }
        if (line.contains(divline1) == true) {
            isNewDiv = true;
        }
        if (module != null) {
            module.AddLine(line);
        }*/
    }

    @Override
    public ParseTreeNode Parse() {
        return treeNode;
    }

    protected String ConvertSubParseName(String title) {
        String result = null;
        String prefix1 = "DUMP OF SERVICE";
        String prefix2 = "dumpsys";
        int prefixLen = prefix1.length();
        int index = title.indexOf(prefix1);
        if (index == -1) {
            index = title.indexOf(prefix2);
            prefixLen = prefix2.length();
        }
        if (index == -1) {
            result = title;
        } else {
            result = title.substring(index + prefixLen, title.length()-1);
        }
        return  result;
    }
}
