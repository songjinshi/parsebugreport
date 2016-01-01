package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;

/**
 * Created by a20023 on 11/13/2015.
 */
public class ModuleDefaultHandler extends Module {
    public ModuleDefaultHandler(String name, String path) {
        displayName = name;
        outputPath = path;
        treeNode =  new ParseTreeNode(displayName, outputPath);
    }

    @Override
    public ParseTreeNode Parse() {
        if (logData.size() <= 2) {
            return null;
        }
        KeepOrignalOutputWithHtmlTag();
        return treeNode;
    }
}
