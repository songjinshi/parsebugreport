package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 11/13/2015.
 */
public class ModuleSUMMARY extends Module {
    public ModuleSUMMARY() {
        outputPath = "summary.html";
        matchList.add(new MatchCAT(Pattern.compile("===================================(.*?)$"),IGNORE));
        matchList.add(new MatchCAT(Pattern.compile("== dumpstate: (.*?)$"), LOGDATE));
        matchList.add(new MatchCAT(Pattern.compile("===================================== (.*?)$"),IGNORE ));
        matchList.add(new MatchCAT(Pattern.compile("Build: (.*?)$"),BUILDVER));
        matchList.add(new MatchCAT(Pattern.compile("Bootloader: (.*?)$"), BLVER));
        matchList.add(new MatchCAT(Pattern.compile("Radio: (.*?)$"), RADIOTYPE));
        matchList.add(new MatchCAT(Pattern.compile("Network: (.*?)$"),NETWORK));
        matchList.add(new MatchCAT(Pattern.compile("Kernel: (.*?)$"),KNLVER ));
        matchList.add(new MatchCAT(Pattern.compile("Command line: (.*?)$"), COMMANDLINE));

        commandList.add("androidboot.serialno");
        commandList.add("androidboot.build_vars");
        commandList.add("movablecore");
        commandList.add("bootreason");
        commandList.add("androidboot.carrier");
        commandList.add("androidboot.device");
        commandList.add("androidboot.hwrev");
        commandList.add("androidboot.radio");
        commandList.add("androidboot.fsg-id");
        commandList.add("androidboot.cid");
        commandList.add("androidboot.bl_state");
        commandList.add("androidboot.powerup_reason");
        commandList.add("androidboot.write_protect");
        commandList.add("androidboot.mode");
    }

    private class MatchCAT {
        Pattern pattern;
        int cat;
        MatchCAT(Pattern p, int c) {
            pattern = p;
            cat = c;
        }
    }






    private final int IGNORE = -1;
    private final int LOGDATE = 0;
    private final int BUILDVER = 1;
    private final int BLVER = 2;
    private final int RADIOTYPE = 3;
    private final int NETWORK = 4;
    private final int KNLVER = 5;
    private final int COMMANDLINE = 6;
/*    private final int LASTLINE = COMMANDLINE;
    private final int MAXSUPPORT = LASTLINE+1;*/


    private final String summaryArr[] = {"LOG DATE","BUILD VERISON", "BOOTLOAD VERSION", "RADIO TYPE",
            "NETWORK", "KERNL VERSION"};


    private String DIR = "html";




    @Override
    public ParseTreeNode Parse() {

        Iterator it = parseResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.TOPPRORITY,
                    "<div><br><b>"+(String)pair.getKey()+" : </b>"+pair.getValue()+"</div><hr>");
        }

        return null;
    }

    LinkedList<MatchCAT> matchList = new LinkedList<MatchCAT>();
    LinkedList<String> commandList = new LinkedList<String>();
    Map <String, String> parseResultMap = new LinkedHashMap <String, String>();

    @Override
    public void AddLine(String line) {
        super.AddLine(line);

        ListIterator<MatchCAT> listIterator = matchList.listIterator();
        while(listIterator.hasNext()) {
            MatchCAT mc = listIterator.next();
            Pattern p = mc.pattern;
            int c = mc.cat;

            Matcher matcher = p.matcher(line);
            if (matcher.find()) {
                if (c == IGNORE) {
                    continue;
                }
                if (c == COMMANDLINE) {
                    String parameters[] = matcher.group(1).split(" ");

                    for (int i = 0; i < parameters.length; i++) {
                        ListIterator<String> cIt = commandList.listIterator();
                        while (cIt.hasNext()) {
                            String str = cIt.next();
                            Pattern paramPattern = Pattern.compile(str+"=(.*?)$");
                           // System.out.println(str);
                            Matcher paramMatcher = paramPattern.matcher(parameters[i]);
                            if (paramMatcher.find()) {
                                parseResultMap.put(str, paramMatcher.group(1));
                                cIt.remove();
                            }
                        }
                    }
                } else {
                    parseResultMap.put(summaryArr[c], matcher.group(1));
                }
                listIterator.remove();
            }
        }
    }

}
