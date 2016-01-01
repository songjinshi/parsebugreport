package com.juanhoo.Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;

/**
 * Created by a20023 on 12/23/2015.
 */
public class PredefinedNode {
    ParseTreeNode node;
    PriorityQueue<PriorityInfo> priInfoQue;
    public static final int DEFUALTPRIORITY = 100;
    public static final int TOPPRORITY = 1;
    public static final int BOTTOMPRIORITY = 999;

    protected class PriorityInfo implements Comparable<PriorityInfo>{
        int priority;
        String data;

        PriorityInfo (int pri, String info) {
            priority = pri;
            this.data = info;
        }

        @Override
        public int compareTo(PriorityInfo o) {
            if (o.priority < this.priority) {
                return 1;
            } else if (o.priority > this.priority) {
                return -1;
            }
            return 0;
        }
    }


    PredefinedNode (ParseTreeNode n) {
        node = n;
        priInfoQue = new PriorityQueue<>();
    }

    protected void AddInfoToNode (int pri, String data) {
        priInfoQue.add(new PriorityInfo(pri, data));
    }

    protected void GenerateHtmlReport() {
        try {
            String fileName = node.resultHTMLPath;
            if (fileName == null) {
                return;
            }

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(ParsefileHandle.OUTPUTDIR+"\\"+fileName, true)))) {
                while (!priInfoQue.isEmpty()) {
                    PriorityInfo info = priInfoQue.poll();
                    writer.println(info.data);
                }
                writer.close();
                //more code
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }
}