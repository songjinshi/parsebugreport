package com.juanhoo.Runtime;

/**
 * Created by a20023 on 12/28/2015.
 */
public class ProcessInfo {
    int user;
    int uid;
    int processName;
    int packageName;
    String status;
    String wChan; //stack top
    long startTime;
    long endTime;
    int pid;
    //com.google.android.gms:car  1010019 u10_a19
    //u0_a13  10013  21686 13993 1568468 51468 bg  SyS_epoll_ 0000000000 S com.android.defcontainer

    @Override
    public String toString() {
        String result = "";
        result += "UID:"+uid;
        if (status.equalsIgnoreCase("s")) {
            result += "Status: Sleeping";
        } else if (status.equalsIgnoreCase("r")) {
            result += "Status: Running";
        } else if (status.equalsIgnoreCase("d")) {
            result += "Status: Die";
        }
        return result;
    }
}
