package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/23/2015.
 */
public class ModuleIPV4ADDRESSES extends Module{
    public ModuleIPV4ADDRESSES() {
        moduleName = "IPV4Address";
        displayName = "IPV4 Address";
        outputPath = "ipv4address.html";
    }
    /*
    1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
6: rmnet_data0: <ALLMULTI,PROMISC,UP,LOWER_UP> mtu 1428 qdisc htb state UNKNOWN qlen 1000
    inet 100.80.178.53/30 scope global rmnet_data0
       valid_lft forever preferred_lft forever
32: wlan0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq master bridge0 state UP qlen 1000
    inet 192.168.43.1/24 brd 192.168.43.255 scope global wlan0
       valid_lft forever preferred_lft forever
     */

    String ipv4Info = "";

    @Override
    public void AddLine(String line) {
        super.AddLine(line);
        String pattern = "(.*?): (.*?): <(.*?)> mtu ([0-9]*?) qdisc (.*?) state (.*?)";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            ipv4Info += "<br><b>Interface</b> :<font color=\"blue\">"+match.group(2).toUpperCase() + "</font> <b>Mode</b>: "+ match.group(3) +
                    " <b>MTU</b>: "+match.group(4);
            //AppendInfoToFile(ParsefileHandle.DATACONNFILEID, PredefinedNode.BOTTOMPRIORITY, info);

            return;
        }
        pattern = "^\\s*?inet (.*?) scope (.*?)$";
        match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            ipv4Info += " <b>IPV4 Address</b> :"+match.group(1) + " <b>Scope</b>: "+ match.group(2);

            return;
        }


      //  AppendInfoToFile(ParsefileHandle.DATACONNFILEID, PredefinedNode.TOPPRORITY, line);
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.DATACONNFILEID, PredefinedNode.BOTTOMPRIORITY, ipv4Info);
        return super.Parse();
    }
}
