package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

/**
 * Created by a20023 on 12/24/2015.
 */
public class ModuleSYSTEMPROPERTIES extends Module{

    public ModuleSYSTEMPROPERTIES() {
        moduleName = "SYSTEMPROPERTIES";
        displayName = "System Properties";
        outputPath = "systemproperties.html";
    }
    String productInfo = "<hr>";
    String buildInfo  = "<hr>";
    String netInfo = "<hr>";
    String imsInfo = "<hr>";
    String bootInfo = "<hr>";
    String audioInfo = "<hr>";
    String warningInfo  = "<hr>";

    @Override
    public void AddLine(String line) {
        if (line.contains("ro.product")) {
            productInfo += "<br>"+line;
        } else if (line.contains("ro.build")) {
            buildInfo += "<br>" + line;
        } else if (line.contains("[net.")) {
            netInfo += "<br>" + line;
        } else if (line.contains("[persist.ims")) {
            imsInfo += "<br>" + line;
        } else if (line.contains("ro.boot")) {
            bootInfo += "<br>" + line;
        } else if (line.contains("[sys.usb.config]:")) {
            if (line.contains("mtp")) {
                audioInfo += "<br>" + "MTP mode is enabled in system Properties.";
            } else {
                audioInfo += "<br>" + "MTP mode isn't enabled in system Properties.";
            }
        } else if (line.contains("DEVICE_PROVISIONED")) {
            if (line.contains("[0]")) {
                warningInfo += "<br><b style=\"color:red;font-family:courier;\">Warning : Device hasn't been provisioned in system properties.</b>";
            }
        }


        super.AddLine(line);
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.BOTTOMPRIORITY +2, "<hr><br><b>Build Properties</b>"+buildInfo);
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.TOPPRORITY - 1, warningInfo);
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.BOTTOMPRIORITY +2, "<hr><br><b>Boot Properties</b>"+bootInfo);
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.BOTTOMPRIORITY +2,"<hr><br><b>Product Properties</b>"+productInfo);
        AppendInfoToFile(ParsefileHandle.DATACONNFILEID, PredefinedNode.BOTTOMPRIORITY +2,"<hr><br><b>Net Properties</b>"+netInfo);
        AppendInfoToFile(ParsefileHandle.NETWORKFILEID, PredefinedNode.BOTTOMPRIORITY  +2,"<hr><br><b>IMS Properties</b>"+imsInfo);
        AppendInfoToFile(ParsefileHandle.AUDIOFILEID, PredefinedNode.BOTTOMPRIORITY  +2,"<hr><br><b>Audio Properties</b>"+audioInfo);
        return super.Parse();
    }
}
