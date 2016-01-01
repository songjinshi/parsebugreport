package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/29/2015.
 */
public class ModulePING extends Module{
    public ModulePING() {
        moduleName = "PINGTEST";
        displayName = "PING TEST";
        outputPath = "pingtest.html";
    }

    String pingInfo = "";

    private void CheckPINGDestination(String line) {
        String pattern = "^------ PING (.*?) \\(ping -c \\d -i .\\d (.*?)\\) ------";
        String pingResultPattern = "^\\d packets transmitted, \\d received, (.*?) packet loss, time (.*?)";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            pingInfo += "<br>Ping "+match.group(1)+"("+match.group(2)+")";
            return;
        }
        match = Pattern.compile(pingResultPattern).matcher(line);
        if (match.find()) {
            pingInfo += "  <b>" +match.group(1)+ "</b> packet loss";
            return;
        }
    }

    @Override
    public void AddLine(String line) {
        super.AddLine(line);
        CheckPINGDestination(line);
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.NETWORKFILEID, "<hr>"+pingInfo);
        return super.Parse();
    }
}
