package com.juanhoo.util;

import java.util.Comparator;

/**
 * Created by a20023 on 12/18/2015.
 */
public class MergeItem implements Comparator<MergeItem>,Comparable<MergeItem>{
    int fileInd;
    long time;
    String data;
    public String debugInfo;

    MergeItem(int ind, long t, String line) {
        fileInd = ind;
        time = t;
        data = line;
    }

    @Override
    public int compare(MergeItem o1, MergeItem o2) {
        if (o1.time > o2.time) {
            return 1;
        }
        if (o1.time < o2.time) {
            return -1;
        }
        return 0;
    }

    @Override
    public int compareTo(MergeItem o) {
        if (o.time < time) {
            return 1;
        }
        if (o.time > time) {
            return -1;
        }
        return 0;
    }
}
