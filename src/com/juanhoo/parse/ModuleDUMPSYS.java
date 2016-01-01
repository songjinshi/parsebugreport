package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 11/2/2015.
 */
public class ModuleDUMPSYS extends Module {

    public ModuleDUMPSYS() {
        moduleName = "Dump Sys";
        displayName = "Framework Services";
        outputPath = "frameworkservice.html";
        treeNode = new ParseTreeNode(displayName, "frameworkservice.html");
    }

    private String divline = "-------------------------------------------------------------------------------";
    private boolean isNewDiv = false;
    private boolean DEBUG = false;
    private Module module = null;

    ParseTreeNode childNode = null;

    String patternDumpService = "DUMP OF SERVICE (.*?):";
    String patternDumpSys = ".*? \\(dumpsys(.*?)\\)$";


    public void AddLine (String line) {
        if (line.length() == 0 || line.compareToIgnoreCase(divline) == 0) {
            return;
        }

        Matcher matchDumpService = Pattern.compile(patternDumpService).matcher(line);
        Matcher matchDumpSys = Pattern.compile(patternDumpSys).matcher(line);
        boolean findDumpService = matchDumpService.find();
        boolean findDumpSys = matchDumpSys.find();

        if ( findDumpService ||findDumpSys) {
            if (module != null) {
                childNode = module.Parse();
                treeNode.childArray.add(childNode);
                module = null;
            }
            String serviceName = (findDumpService)?matchDumpService.group(1):matchDumpSys.group(1);
            String className = serviceName.replace(" ","").replace(".","_").toUpperCase();
            String moduleName = "com.juanhoo.dumpsys.Module" + className;
            System.out.println(line);
            System.out.println(moduleName); //Type

            try {

                Class cl =  Class.forName(moduleName);
                module = (Module) cl.newInstance();
            } catch (ClassNotFoundException e) {
                if (DEBUG) { System.out.print("No class found for "+moduleName+"\n");}
            } catch (InstantiationException e) {
                if (DEBUG) {  System.out.print("Instantiation exception " + className + "\n");}
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                if (DEBUG) {  System.out.print("Illgeal access exception " + className
                        + " "+ "\n");}

            }
            if (module == null ){
                module = new ModuleDefaultHandler(serviceName, className+".html");
            }
        }
        if (module != null) {
            module.AddLine(line);
        }


    }

    @Override
    public ParseTreeNode Parse() {
        if (module != null) {
            childNode = module.Parse();
            treeNode.childArray.add(childNode);
            module = null;
        }
        return treeNode;
    }


}
