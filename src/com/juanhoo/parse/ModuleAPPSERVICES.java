package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/2/2015.
 */
public class ModuleAPPSERVICES extends Module{
    public ModuleAPPSERVICES() {
        moduleName = "Activity Services";
        displayName = "Activty Services";
        outputPath = "activityservices.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }
    private boolean DEBUG = false;

    @Override
    public ParseTreeNode Parse() {
        return treeNode;
    }

    String servicePatternName = "^SERVICE (.*?) ";
    String providerPatternName = "^PROVIDER .* .* (.*?)}";
    String ignorePattern = "nothing to dump";


    private String GenerateModuleName(String serviceName) {

        Matcher match = Pattern.compile("(.*?).(\\w*?)$").matcher(serviceName);
        if (match.find()) {
            return "com.juanhoo.dumpservice.Module"+match.group(2);
        }
        return serviceName;
    }

    private Module module = null;
    private ParseTreeNode childNode = null;
    @Override
    public void AddLine(String line) {

        if (line.length() == 0) {
            return;
        }

        Matcher match = Pattern.compile("^\\s*$").matcher(line);
        if (match.find()) {
            return;
        }

        Pattern servicePattern = Pattern.compile(servicePatternName);
        Pattern providerPattern = Pattern.compile(providerPatternName);
        Matcher serviceMatch = servicePattern.matcher(line);
        Matcher providerMatch = providerPattern.matcher(line);
        if (serviceMatch.find()) {
            match = serviceMatch;
        } else if (providerMatch.find()) {
            match = providerMatch;
        } else {
            match = null;
        }

        String childModuleName = null;

        if (match != null) {
            String serviceName = match.group(1).replace('/', '_');
            if (module != null) {
                childNode = module.Parse();
                treeNode.childArray.add(childNode);
                module = null;
            }
            childModuleName = GenerateModuleName(serviceName);
            System.out.println(childModuleName); //Type

            try {

                Class cl =  Class.forName(childModuleName);
                module = (Module) cl.newInstance();

            } catch (ClassNotFoundException e) {
                if (DEBUG) { System.out.print("No class found for "+moduleName+"\n");}
            } catch (InstantiationException e) {
                if (DEBUG) {  System.out.print("Instantiation exception " + moduleName + "\n");}
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                if (DEBUG) {  System.out.print("Illgeal access exception " + moduleName
                        + " "+ "\n");}
            }
            if (module == null) {
                module = new ModuleDefaultHandler(childModuleName, childModuleName+".html");
            }

        }

        match = Pattern.compile(ignorePattern).matcher(line);
        if (match.find()) {
            module = null;
        }

        if (module != null) {
            module.AddLine(line);
        }
    }



}
