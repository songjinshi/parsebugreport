package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;

/**
 * Created by a20023 on 11/3/2015.
 */
public class ModuleFileLog extends Module {

    public ModuleFileLog(String name, String filePath) {
        moduleName = name;
        outputPath = filePath;

    }

    public ModuleFileLog() {

    }

    @Override
    public ParseTreeNode Parse() {
        KeepOrignalOutput();
        /* TODO */
        return null;
    }
}
