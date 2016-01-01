package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/24/2015.
 */
public class ModuleIPV6ADDRESSES  extends Module{
    public ModuleIPV6ADDRESSES() {
        moduleName = "IPV6Address";
        displayName = "IPV6 Address";
        outputPath = "ipv6address.html";
    }
    /*
    ------ IPv6 ADDRESSES (ip -6 addr show) ------
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536
    inet6 ::1/128 scope host
       valid_lft forever preferred_lft forever
2: dummy0: <BROADCAST,NOARP,UP,LOWER_UP> mtu 1500
    inet6 fe80::943c:46ff:fe6e:d411/64 scope link
       valid_lft forever preferred_lft forever
27: wlan0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qlen 1000
    inet6 fe80::5e51:88ff:fe0d:ab2d/64 scope link
       valid_lft forever preferred_lft forever

     */

    String ipv6Info = "";

    @Override
    public void AddLine(String line) {
        super.AddLine(line);
        String pattern = "(.*?): (.*?): <(.*?)> mtu (\\d{1,5}?) (.*?)$";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {

            ipv6Info += "<br><b>Interface</b> :<font color=\"blue\">"+match.group(2).toUpperCase() + "</font> <b>Mode</b>: "+ match.group(3) +
                    " <b>MTU</b>: "+match.group(4);

            return;
        }
        pattern = "^\\s*?inet6 (.*?) scope (.*?)$";
        match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            ipv6Info += " <b>IPV6 Address</b> :"+match.group(1) + " <b>Scope</b>: "+ match.group(2);
            return;
        }


        //  AppendInfoToFile(ParsefileHandle.DATACONNFILEID, PredefinedNode.TOPPRORITY, line);
    }

    @Override
    public ParseTreeNode Parse() {
        super.Parse();

        AppendInfoToFile(ParsefileHandle.DATACONNFILEID, PredefinedNode.BOTTOMPRIORITY, ipv6Info);
        return new ParseTreeNode(displayName, outputPath);
    }
}

