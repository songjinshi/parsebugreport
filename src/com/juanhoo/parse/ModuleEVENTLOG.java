package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 11/3/2015.
 */
public class ModuleEVENTLOG extends ModuleFileLog{

    public ModuleEVENTLOG() {
        moduleName = "Event_log";
        outputPath = "event_log.txt";
        ParsefileHandle.AddFileToMergeList(ParsefileHandle.OUTPUTDIR+"\\"+outputPath);
    }
    ///** 30008 am_anr (User|1|5),(pid|1|5),(Package Name|3),(Flags|1|5),(reason|3) */
//12-27 23:21:11.343  1157  1565 I am_anr  : [10,7999,com.google.android.gms.persistent,-1060618555,executing service com.google.android.gms/com.google.android.location.fused.FusedLocationService]
    //  /** 30039 am_crash (User|1|5),(PID|1|5),(Process Name|3),(Flags|1|5),(Exception|3),(Message|3),(File|3),(Line|1|5) */
    Vector<String> anrVector = new Vector<>();
    Vector<String> crashVector = new Vector<>();
    Vector<String> longLaunchTimeVector = new Vector<>();

    //am_kill (User|1|5),(PID|1|5),(Process Name|3),(OomAdj|1|5),(Reason|3)
    //12-27 23:19:35.611  1157  1663 I am_activity_launch_time: [0,548857672,com.google.android.googlequicksearchbox/com.google.android.launcher.GEL,1744,1744]//
    /** 30009 am_activity_launch_time (User|1|5),(Token|1|5),(Component Name|3),(time|2|3) */

    @Override
    public void AddLine(String line) {
        if (line.contains("am_anr")) {
            anrVector.add(line);
            return;
        } else if (line.contains("am_crash")) {
            crashVector.add(line);
            return;
        }

        String launchPattern = "(^[\\d-]{5}) ([\\d:.]{12})  ([\\d]{4})  ([\\d]{4}) I am_activity_launch_time: \\[([\\d-]+?),([\\d-]+?),([\\w./]*?),([\\d]+?),([\\d]+?)\\]";
        Matcher match = Pattern.compile(launchPattern).matcher(line);
        if (match.find()) {
            String packageName = match.group(7);
            int displayTime = Integer.parseInt(match.group(8));
            int totalLanuchTime = Integer.parseInt(match.group(9));

            if (displayTime > 1000 || totalLanuchTime > 1000) {
                longLaunchTimeVector.add(line);
            }
        }

        super.AddLine(line);
    }

    @Override
    public ParseTreeNode Parse() {
        int anrNumber = anrVector.size();
        if (anrNumber > 0) {
            String anrInfo = "<br>Total "+anrNumber+ " ANRs in the bugreport.";
            for (int i = 0; i < anrVector.size(); i++) {
                anrInfo += "<br>"+anrVector.get(i);
            }
            AppendInfoToFile(ParsefileHandle.STABFILEID, PredefinedNode.TOPPRORITY+2, anrInfo);
        }

        int crashNumber = crashVector.size();
        if (crashNumber > 0) {
            String crashInfo = "<br>Total "+crashNumber+ " Crash in the bugreport.";
            for (int i = 0; i < crashVector.size(); i++) {
                crashInfo += "<br>"+crashVector.get(i);
            }
            AppendInfoToFile(ParsefileHandle.STABFILEID, PredefinedNode.TOPPRORITY+3, crashInfo);
        }
        int longLaunchNum = longLaunchTimeVector.size();
        if (longLaunchNum > 0) {
            String longLaunchInfo = "<br>Total "+longLaunchNum+ " application launch time exceed 1000ms.";
            for (int i = 0; i < longLaunchTimeVector.size(); i++) {
                longLaunchInfo += "<br>"+longLaunchTimeVector.get(i);
            }
            AppendInfoToFile(ParsefileHandle.STABFILEID, PredefinedNode.TOPPRORITY+4, longLaunchInfo);
        }

        return super.Parse();
    }
}
