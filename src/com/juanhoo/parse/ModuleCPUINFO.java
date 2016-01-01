package com.juanhoo.parse;

/**
 * Created by a20023 on 11/14/2015.
 */
//ModuleCPUINFO
    //top -n 1 -d 1 -m 30 -t

/*
system/core/toolbox/top.c
Usage: top [ -m max_procs ] [ -n iterations ] [ -d delay ] [ -s sort_column ] [ -t ] [ -h ]
        2
        -m num  Maximum number of processes to display.
        3
        -n num  Updates to show before exiting.
        4
        -d num  Seconds to wait between updates.
        5
        -s col  Column to sort by (cpu,vss,rss,thr).
        6
        -t      Show threads instead of processes.
        7
        -h      Display this help screen.
*/

public class ModuleCPUINFO extends Module {
    public ModuleCPUINFO() {
        moduleName = "CPU INFO";
        displayName = "CPU INFO";
        outputPath = "cpuinfo.html";
    }
}
