package com.juanhoo.Runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by a20023 on 12/28/2015.
 */
public class PhoneRunningInfo {
    HashMap<Integer, ProcessInfo> processInfoByPIDHashMap;
    HashMap<Integer, HashMap<Integer, ProcessInfo>> processInfoByUIDHashMap;

    public void AddProcessRecord(ProcessInfo processInfo) {
        processInfoByPIDHashMap.put(processInfo.pid, processInfo);

        HashMap<Integer,ProcessInfo> uidProcessInfoMap = processInfoByUIDHashMap.get(processInfo.pid);
        if (uidProcessInfoMap == null) {
            uidProcessInfoMap = new HashMap<Integer, ProcessInfo>();
            uidProcessInfoMap.put(processInfo.pid, processInfo);
            processInfoByUIDHashMap.put(processInfo.uid, uidProcessInfoMap);
        } else {
            if (!uidProcessInfoMap.containsKey(processInfo.pid)) {
                uidProcessInfoMap.put(processInfo.pid, processInfo);
            }
        }
    }

    public ProcessInfo GetProcessRecordByPID(int pid) {
        return null;
    }


}
