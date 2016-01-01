package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.FileHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/24/2015.
 */
public class ModuleFILESYSTEMSFREESPACE  extends Module {
    public ModuleFILESYSTEMSFREESPACE() {
        moduleName = "ModuleFILESYSTEMSFREESPACE";
        displayName = "System space";
        outputPath = "filesystemfreespace.html";
    }

    private String warningInfo = "";
    private String info = "";

    private void CreateWarningWithLowSpace(String line) {
        String pattern = "([^\\s]++)\\s++([0-9.GMK]++)\\s++([0-9.GMK]++)\\s++([0-9.GMK]++)\\s++([0-9]++)";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            double totalSpace = GetSpaceSize(match.group(2));
            double usedSpace = GetSpaceSize(match.group(3));
            double freeSpace = GetSpaceSize(match.group(4));
            String fileSysName = match.group(1);
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);
            double usedRatio = usedSpace / totalSpace;
            if (usedRatio > 0.9) {
                warningInfo += "<br>";
                warningInfo += "<b style=\"color:red;\">";
                warningInfo += "File System " + fileSysName + " used/total: " + match.group(3) + "/" + match.group(2);
                warningInfo += "(" + df.format((usedSpace / totalSpace) * 100) + " %) </b>";
            }
            info += "<br>";
            info += "File System " + fileSysName + " used/total: " + match.group(3) + "/" + match.group(2);
            info += "(" + df.format((usedSpace / totalSpace) * 100) + " %) ";


        }

    }

    private Double GetSpaceSize(String data ) {
        String pattern = "([0-9.]++)([GKM])";
        double size = 0;
        Matcher match = Pattern.compile(pattern).matcher(data);
        if (match.find()) {
            size = Double.parseDouble(match.group(1));
            if (match.group(2).equalsIgnoreCase("G")) {
                size = size * 1024 * 1024;
            } else if ((match.group(2).equalsIgnoreCase("M"))){
                size = size * 1024;
            }
        }
        System.out.println(size);
        return size;
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.TOPPRORITY - 1, warningInfo);
        AppendInfoToFile(ParsefileHandle.MEMORYFILEID,  "<hr>" + info );
        return super.Parse();
    }

    @Override
    public void AddLine(String line) {
        CreateWarningWithLowSpace(line);
        super.AddLine(line);
    }
}