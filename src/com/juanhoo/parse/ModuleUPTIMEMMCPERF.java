package com.juanhoo.parse;

/**
 * Created by a20023 on 11/14/2015.
 */
/*
dump_files("UPTIME MMC PERF", mmcblk0, skip_not_stat, dump_stat_from_fd);
/sys/block/mmcblk0/
 //In android mmcblk0 is the internal NAND and mmcblk1 is the external sd card device
 */
public class ModuleUPTIMEMMCPERF extends Module {
    public ModuleUPTIMEMMCPERF() {
        moduleName = "UPTIME MMC PERF";
        displayName = "UPTIME MMC PERF";
        outputPath = "uptimemmcpref.html";
    }
}

