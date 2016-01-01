package com.juanhoo.Controller;

import com.juanhoo.parse.ModuleDefaultHandler;
import com.juanhoo.parse.ModuleSUMMARY;
import com.juanhoo.parse.Module;
import com.juanhoo.util.MultiQueueMergeLog;

import javax.swing.tree.TreeNode;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 10/27/2015.
 */
public class ParsefileHandle {
    private String fileName = null;
    private final String SECTIONSTART = "------ ";
    private final String SECTIONEND = " ------";
    private File parseFile = null;


    ParseLogController UIController = null;

    public ParsefileHandle(String name) {
        fileName = name;
    }

    public final static String OUTPUTDIR = "html";

    public ParsefileHandle(File f) {
        parseFile = f;
    }

    public void SetUIController(ParseLogController controller) {
        UIController = controller;
    }

    public static boolean DEBUG = false;

    public final static String STABNODE = "Stability";
    public final static String AUDIONODE = "Audio";
    public final static String CONNNODE = "Connectivity";
    public final static String PKGNODE = "Package";
    public final static String SETTINGNODE = "Settings";
    public final static String DATACONNNODE = "Data Connection";
    public final static String NETWORKNODE = "Network Status";
    public final static String OVERVIEWNODE = "Overview";
    public final static String MEMORYNODE = "Memory";
    public final static String STABNODEREPORTFILE = STABNODE + "_summary.html";
    public final static String AUDIONODEREPORTFILE = AUDIONODE + "_summary.html";
    public final static String CONNNODEREPORTFILE = CONNNODE + "_summary.html";
    public final static String PKGNODEREPORTFILE = PKGNODE + "_summary.html";
    public final static String SETTINGNODEREPORTFILE = SETTINGNODE + "_summary.html";
    public final static String OVERVIEWNODEREPORTFILE = OVERVIEWNODE + "_summary.html";
    public final static String DATACONNNODEREPORTFILE = DATACONNNODE + "_summary.html";
    public final static String NETWORKNODEREPORTFILE = NETWORKNODE + "_summary.html";
    public final static String MEMORYNODEREPORTFILE = MEMORYNODE + "_summary.html";


    public static final Integer OVERVIEWFILEID = 0;
    public static final Integer STABFILEID = 1;
    public static final Integer AUDIOFILEID = 2;
    public static final Integer CONNECTIVITYFILEID = 3;
    public static final Integer PKGFILEID = 4;
    public static final Integer DATACONNFILEID = 5;
    public static final Integer NETWORKFILEID = 6;
    public static final Integer MEMORYFILEID = 7;
    public static final Integer MAXPREDEFINEDFILEID = MEMORYFILEID;

    private static ArrayList<String> mergeFileList = new ArrayList<>();


    public static void AddFileToMergeList(String fileName) {
        // mergeFileList.add(OUTPUTDIR+"\\"+fileName);
        mergeFileList.add(fileName);
    }


    private static HashMap<Integer, PredefinedNode> predefinedNodeMap = new HashMap<>();

    {
        predefinedNodeMap.put(OVERVIEWFILEID, new PredefinedNode(new ParseTreeNode(OVERVIEWNODE, OVERVIEWNODEREPORTFILE)));
        predefinedNodeMap.put(STABFILEID, new PredefinedNode(new ParseTreeNode(STABNODE, STABNODEREPORTFILE)));
        predefinedNodeMap.put(AUDIOFILEID, new PredefinedNode(new ParseTreeNode(AUDIONODE, AUDIONODEREPORTFILE)));
        predefinedNodeMap.put(CONNECTIVITYFILEID, new PredefinedNode(new ParseTreeNode(CONNNODE, CONNNODEREPORTFILE)));
        predefinedNodeMap.put(PKGFILEID, new PredefinedNode(new ParseTreeNode(PKGNODE, PKGNODEREPORTFILE)));
        predefinedNodeMap.put(DATACONNFILEID, new PredefinedNode(new ParseTreeNode(DATACONNNODE, DATACONNNODEREPORTFILE)));
        predefinedNodeMap.put(NETWORKFILEID, new PredefinedNode(new ParseTreeNode(NETWORKNODE, NETWORKNODEREPORTFILE)));
        predefinedNodeMap.put(MEMORYFILEID, new PredefinedNode(new ParseTreeNode(MEMORYNODE, MEMORYNODEREPORTFILE)));
    }

    public static ParseTreeNode GetNodeByID(int fileId) {

        ParseTreeNode node = predefinedNodeMap.get(fileId).node;
        return node;
    }

    public static void AddInfoToPredfineFile(int fileId, int priority, String data) {

        PredefinedNode predfinedNode = predefinedNodeMap.get(fileId);
        if (predfinedNode == null) {
            System.out.println("No predefined file for File ID " + fileId);
        }
        predfinedNode.AddInfoToNode(priority, data);
        return;
    }


    public static HashMap<String, PackageInfo> packagesCollection = new HashMap<String, PackageInfo>();

    private void ClearHistoryFile(String dir) {
        File path = new File(dir);
        if (path == null) {
            return;
        }

        if (!path.exists()) {
            return;
        }

        for (File file : path.listFiles()) {
            if (file != null) {
                file.delete();
            }
        }
    }





    private class PredefineModuleName {
        String pattern;
        String predefineModuleName;

        PredefineModuleName(String namePattern, String moduleName) {
            pattern = namePattern;
            predefineModuleName = moduleName;
        }
    }

    Vector<PredefineModuleName> predefineModuleVector = new Vector<>();

    {
        predefineModuleVector.add(new PredefineModuleName("^SHOW MAP(.*?)", "com.juanhoo.parse.ModuleSHOWMAP"));
        predefineModuleVector.add(new PredefineModuleName("^PING(.*?)", "com.juanhoo.parse.ModulePING"));
        predefineModuleVector.add(new PredefineModuleName("^ACCEPT_RA(.*?)", "com.juanhoo.parse.ModuleACCEPTRA"));
        predefineModuleVector.add(new PredefineModuleName("^ROUTE TABLE (.*?)", "com.juanhoo.parse.ModuleROUTETABLE"));
    }

    private String GetPredfinedModuleName(String title) {
        for (int i = 0; i < predefineModuleVector.size(); i++) {
            PredefineModuleName predefineModuleNameNode = predefineModuleVector.get(i);
            Matcher match = Pattern.compile(predefineModuleNameNode.pattern).matcher(title);
            if (match.find()) {
                return predefineModuleNameNode.predefineModuleName;
            }
        }
        return null;
    }


    public void ParseFile() {
        if (parseFile == null) {
            return;
        }
        ClearHistoryFile(OUTPUTDIR);

        try (BufferedReader br = new BufferedReader(new FileReader(parseFile))) {
            String line;
            int i = 0;

            Module currentModule = null;
            ModuleSUMMARY modsummary = new ModuleSUMMARY();
            //Get product's name to create root node, and basic information.
            while ((line = br.readLine()) != null && (getRawSection(line) == null)) {
                if (line.length() == 0) {
                    continue;
                }
                modsummary.AddLine(line);
            }
            modsummary.Parse();


            UIController.InitTree(GetNodeByID(OVERVIEWFILEID));

            for (PredefinedNode predefinedNode : predefinedNodeMap.values()) {
                if (!predefinedNode.node.nodeName.equalsIgnoreCase(OVERVIEWNODE)) {
                    UIController.AddTreeNode(predefinedNode.node);
                }
            }


            ParseTreeNode rawNode = new ParseTreeNode("Raw Data", "rawdata.html");


            do {
                //System.out.print(line+"\n");
                String str = getRawSection(line);
                if (str != null) {
                    String itemName = getTitleByRawSection(str);
                    //System.out.println(itemName);
                    String predefinedModuleName = GetPredfinedModuleName(itemName);
                    String sectionTitle = itemName.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
                    String moduleName = (predefinedModuleName != null) ?
                            predefinedModuleName : "com.juanhoo.parse.Module" + sectionTitle;

                    if (currentModule != null) {
                        if (!currentModule.getClass().getName().contains(moduleName)) {
                            ParseTreeNode node = currentModule.Parse();
                            if (node != null) {
                                rawNode.AddChildNode(node);
                            }
                            currentModule = null;
                            System.out.println(moduleName);

                        } else {
                            if (line.length() != 0) {
                                currentModule.AddLine(line);
                            }
                            continue;
                        }
                    }
                    try {
                        Class cl = Class.forName(moduleName);
                        currentModule = (Module) cl.newInstance();
                    } catch (ClassNotFoundException e) {
                        if (DEBUG) {
                            System.out.print("No class found for " + moduleName + "\n");
                        }
                    } catch (InstantiationException e) {
                        if (DEBUG) {
                            System.out.print("Instantiation exception " + sectionTitle + "\n");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        if (DEBUG) {
                            System.out.print("Illgeal access exception " + sectionTitle + " " + "\n");
                        }

                    }
                    if (currentModule == null) {
                        currentModule = new ModuleDefaultHandler(itemName, moduleName + ".html");
                    }


                }
                if (currentModule != null) {
                    if (line.length() != 0) {
                        currentModule.AddLine(line);
                    }
                }

            } while ((line = br.readLine()) != null);

            if (currentModule != null) {
                ParseTreeNode node = currentModule.Parse();
                if (node != null) {
                    // UIController.AddTreeNode(node);
                    rawNode.AddChildNode(node);
                }
            }
            UIController.AddTreeNode(rawNode);

            MultiQueueMergeLog mergeLog = new MultiQueueMergeLog();
            mergeLog.SetInputFileList(mergeFileList.toArray(new String[mergeFileList.size()]));
            mergeLog.SetOutputFileName(OUTPUTDIR + "\\merge.txt");
            mergeLog.Process();
            String logPeriod = "<br>Log caught between " + mergeLog.GetLogStartTime() + " -- " + mergeLog.GetLogEndTime();
            AddInfoToPredfineFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.TOPPRORITY - 3, logPeriod);

            for (PredefinedNode predefinedNode : predefinedNodeMap.values()) {
                predefinedNode.GenerateHtmlReport();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumber(String string) {
        try {
            Long.parseLong(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String getTitleByRawSection(String rawSectionString) {
        if (rawSectionString == null) {
            return null;
        }
        int index = rawSectionString.indexOf("(");
        if (index != -1) {
            rawSectionString = rawSectionString.substring(0, index - 1);
        }
        int lastInd = rawSectionString.lastIndexOf(" ");
        if (lastInd != -1) {
            String endStr = rawSectionString.substring(lastInd + 1, rawSectionString.length());
            /*if (isNumber(endStr)) {  //For example: Show Map 1
                rawSectionString = rawSectionString.substring(0, lastInd);
            }*/
        }
        return rawSectionString;
    }


    public String getRawSection(String line) {

        if (line == null) {
            return null;
        }

        boolean isSection = false;

        int starttokenlen = SECTIONSTART.length();
        int endtokenlen = SECTIONEND.length();
        int strlen = line.length();

        if ((starttokenlen + endtokenlen) > strlen) {
            return null;
        }
        String subStr = line.substring(0, starttokenlen);
        //System.out.print(subStr+"\n");
        if (subStr.equals(SECTIONSTART) == false) {
            return null;
        }
        subStr = line.substring(strlen - endtokenlen, strlen);
        //System.out.print(subStr+"\n");
        if (subStr.equals(SECTIONEND) == false) {
            return null;
        }
        subStr = line.substring(starttokenlen, strlen - endtokenlen);

        return subStr;
    }

}
