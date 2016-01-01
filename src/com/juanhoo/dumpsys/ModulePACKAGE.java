package com.juanhoo.dumpsys;

import com.juanhoo.Controller.*;
import com.juanhoo.Controller.PackageInfo;
import com.juanhoo.parse.Module;

import java.util.Vector;

/**
 * Created by a20023 on 11/5/2015.
 */
public class ModulePACKAGE extends Module {
//DUMP OF SERVICE package:
    public ModulePACKAGE() {
        moduleName = "Package service";
        displayName = "Package";
        outputPath = "packageservice.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }


    private PackageInfo pkg = null;

    private int packageNum = 0;
    private Vector<String> manualInstallPackage = new Vector<String>();

    @Override
    public ParseTreeNode Parse() {
        String info = "<div><br><b>Total package number : </b>"+packageNum+
                "<b> User Installed package number: </b>"+manualInstallPackage.size()+"</div>";

        for (int i = 0; i < manualInstallPackage.size(); i++) {
            String installPackageName = manualInstallPackage.get(i);
            PackageInfo pkg = ParsefileHandle.packagesCollection.get(installPackageName);
            info +=  pkg.toString()+"<hr>";
        }
        AppendInfoToFile(ParsefileHandle.PKGFILEID,info);
        return super.Parse();
    }

    String packageName = null;

    @Override
    public void AddLine (String line) {
        if (line.indexOf("Package [") != -1) {
                if (pkg != null) {
                    ParsefileHandle.packagesCollection.put(packageName, pkg);
                }
                pkg = new PackageInfo();
                packageName = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                pkg.SetPackageName(packageName);
                packageNum++;
                logData.add(packageName);
        } else if (line.indexOf("pkgFlags=[") != -1) {
            if (line.contains("SYSTEM") == false) {
                manualInstallPackage.add(packageName);
            }
        }
        if (pkg != null) {
            pkg.ParseData(line);
        }
    }
}
