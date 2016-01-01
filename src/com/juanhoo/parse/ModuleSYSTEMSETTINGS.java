package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.dumpservice.ModuleSettingsProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/28/2015.
 */
public class ModuleSYSTEMSETTINGS extends ModuleSettingsProvider {
    public ModuleSYSTEMSETTINGS() {
        moduleName = "SystemSettings";
        displayName = "System Settings";
        outputPath = "Systemsetting.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }

    //------ SYSTEM SETTINGS (user 10)
    private void GetUserId(String line) {
        String pattern = "^------ SYSTEM SETTINGS \\(user ([\\d]{1,2})\\).*?";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            userId = match.group(1);
        }
    }

    @Override
    public void AddLine(String line) {
        GetUserId(line);
        super.AddLine(line);
    }

    @Override
    public ParseTreeNode Parse() {
        super.Parse();
        return treeNode;
    }
}
