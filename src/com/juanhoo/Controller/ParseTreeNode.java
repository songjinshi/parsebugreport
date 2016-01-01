package com.juanhoo.Controller;

import java.util.ArrayList;

/**
 * Created by a20023 on 11/12/2015.
 */
public class ParseTreeNode {
    public String nodeName;
    public String parentNodeName;
    public String resultHTMLPath;
    public ArrayList<ParseTreeNode> childArray;

    public ParseTreeNode(String name,String filePath) {
        nodeName = name;
        resultHTMLPath = filePath;
        childArray = new  ArrayList<ParseTreeNode>();
    }

    public ParseTreeNode(String name, String parentName, String filePath) {
        nodeName = name;
        parentNodeName = parentName;
        resultHTMLPath = filePath;
        childArray = new  ArrayList<ParseTreeNode>();
    }

    public ParseTreeNode AddChildNode(ParseTreeNode childNode) {
        childArray.add(childNode);
        return this;
    }

    public String toString() {
        return nodeName;
    }
}
