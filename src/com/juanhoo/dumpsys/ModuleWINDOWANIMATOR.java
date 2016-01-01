package com.juanhoo.dumpsys;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.parse.Module;

/**
 * Created by a20023 on 11/3/2015.
 */
public class ModuleWINDOWANIMATOR extends Module {

    public ModuleWINDOWANIMATOR() {
        moduleName = "Window Animator";
        displayName = "Window Animator";
        outputPath = "windowanimator.html";
        treeNode =  new ParseTreeNode(displayName, outputPath);
    }

    @Override
    public ParseTreeNode Parse() {
        KeepOrignalOutputWithHtmlFormat();
        return new ParseTreeNode(displayName, outputPath);
    }
}
