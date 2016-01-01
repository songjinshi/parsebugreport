package com.juanhoo.parse;

/**
 * Created by a20023 on 10/28/2015.
 */
//up time: 2 days, 02:34:26, idle time: 14:20:18, sleep time: 1 day, 22:52:22
// system/core/toolbox/uptime.c /proc/uptime
public class ModuleUPTIME  extends Module {

    public ModuleUPTIME( ) {
        moduleName = "Uptime";
        displayName = "UP Time";
        outputPath = "uptime.html";
    }



    @Override
    public void AddLine(String line) {
        super.AddLine(line);
    }
}
