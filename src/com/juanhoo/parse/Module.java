package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by a20023 on 10/28/2015.
 */
public abstract class Module {
    protected String moduleName;
    protected String displayName;
    protected int level;
    protected String outputPath;

    protected ArrayList<String> logData = new  ArrayList<String> ();

    public void AddLine (String line) {
        logData.add(line);
    }
    protected ParseTreeNode treeNode = null;
    protected Vector<String> parseResult = new Vector<>();

    public ParseTreeNode Parse() {
        KeepOrignalOutputWithHtmlTag();
        return new ParseTreeNode(displayName, outputPath);
    }

    protected void KeepOrignalOutputWithHtmlFormat() {  //This one is just for default
        try {
            File outDir = new File(ParsefileHandle.OUTPUTDIR);
            if (!outDir.exists()) {
                outDir.mkdir();
            }

            PrintWriter writer = new PrintWriter("html"+"\\"+outputPath, "UTF-8");
            for (int i = 0; i < logData.size(); i++) {
                writer.println("<div><br>"+logData.get(i)+"</div>");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void KeepOrignalOutputWithHtmlTag() {  //This one is just for default
        try {
            File outDir = new File("html");
            if (!outDir.exists()) {
                outDir.mkdir();
            }

            PrintWriter writer = new PrintWriter("html"+"\\"+outputPath, "UTF-8");
        //    CreateHtmlHeader(writer);
            for (int i = 0; i < logData.size(); i++) {
                writer.println("<br>" + logData.get(i));
            }
           // CreateHtmlTail(writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void KeepOrignalOutput() {  //This one is just for default
        try {
            File outDir = new File("html");
            if (!outDir.exists()) {
                outDir.mkdir();
            }

            PrintWriter writer = new PrintWriter("html"+"\\"+outputPath, "UTF-8");
            //    CreateHtmlHeader(writer);
            for (int i = 0; i < logData.size(); i++) {
                writer.println(logData.get(i));
            }
            // CreateHtmlTail(writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    protected void CreateHtmlHeader(PrintWriter writer) {
        writer.println("<html lang=\"en-US\">");
        writer.println("<head>");
        writer.println("<title>Basic Information</title>");
        writer.println("<style>");
        writer.println("body {background-color: lightgray}");
        writer.println("h1 {color: gray}");
        writer.println("p {color: green}");
        writer.println("div {color: black}");
        writer.println("</style>");
        writer.println("</head>");
        writer.println("<body >");
    }

    protected void CreateHtmlTail(PrintWriter writer) {
        writer.println("</body>");
        writer.println("</html>");
    }



    protected void AppendInfoToFile(int fileId,  String info){

        ParsefileHandle.AddInfoToPredfineFile(fileId, PredefinedNode.DEFUALTPRIORITY, info);

    }

    protected void AppendInfoToFile(int fileID, int pri, String info) {
        ParsefileHandle.AddInfoToPredfineFile(fileID, pri, info);
    }
}
