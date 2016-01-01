package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by a20023 on 11/16/2015.
 */
public class ModulePROCESSESANDTHREADS extends  Module{
    public ModulePROCESSESANDTHREADS() {
        displayName = "Processes and Threads";
        outputPath = "processesthreads.html";
    }

    @Override
    public ParseTreeNode Parse() {
        try {
            String tableStart = "<table style=\"width:100%\">";
            String tableEnd = "</table>";
            String trStart = "<tr>";
            String trEnd = "</tr>";
            String tdStart = "<td>";
            String tdEnd = "</td>";
            File outDir = new File("html");
            String htmlHeader =
                    "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "table, th, td {\n" +
                    "    border: 1px solid black;\n" +
                    "    border-collapse: collapse;\n" +
                    "}\n" +
                    "th, td {\n" +
                    "    padding: 5px;\n" +
                    "    text-align: left;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>";
            String htmlEnd = "</body>\n" +
                    "</html>\n";
            if (!outDir.exists()) {
                outDir.mkdir();
            }
            PrintWriter writer = new PrintWriter("html" + "\\" + outputPath, "UTF-8");
            writer.println(htmlHeader);
            writer.println(tableStart);

            for (int i = 2; i < logData.size(); i++) {
                String line = logData.get(i);
                if (line.contains("-----")) {
                    break;
                }
                String cellArr[] = line.split(" +");
                writer.println(trStart);
                for (int j = 0; j < cellArr.length; j++) {
                    cellArr[j].replace(" +","");
                    writer.println(tdStart + cellArr[j] + tdEnd);
                }
                writer.println(trEnd);
            }
            writer.println(tableEnd);
            writer.println(htmlEnd);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ParseTreeNode(displayName, outputPath);
    }


}
