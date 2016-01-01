package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 11/3/2015.
 */
public class ModuleSYSTEMLOG extends ModuleFileLog {
    public ModuleSYSTEMLOG() {
        moduleName = "System_log";
        outputPath = "System_log.txt";
    }

    //--------- beginning of

    String pattern = "^--------- beginning of (.*?)$";


    ModuleFileLog module = null;
    @Override
    public void AddLine(String line) {
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            String name = match.group(1);
            if (module != null) {
                module.Parse();
                if (module.moduleName.compareToIgnoreCase("Crash") == 0) {
                    try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("html\\"+ ParsefileHandle.STABNODEREPORTFILE, true)))) {

                        for (int i = 0; i < module.logData.size(); i++) {
                            writer.print("<br>"+module.logData.get(i));
                        }
                    }catch (IOException e) {
                        //exception handling left as an exercise for the reader
                    }
                }
                ParsefileHandle.AddFileToMergeList(ParsefileHandle.OUTPUTDIR+"\\"+module.outputPath);
            }

            module = new ModuleFileLog(name, name+".txt");
        }
        if (module != null) {
            module.AddLine(line);
        }
    }

    @Override
    public ParseTreeNode Parse() {
        if (module != null) {
            module.Parse();
            ParsefileHandle.AddFileToMergeList(ParsefileHandle.OUTPUTDIR+"\\"+module.outputPath);
        }
        return null;
    }
}
