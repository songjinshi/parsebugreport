package com.juanhoo.dumpsys;

import com.juanhoo.Controller.PackageInfo;
import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;
import com.juanhoo.parse.Module;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 11/29/2015.
 */
public class ModuleACCOUNT extends Module {
    public ModuleACCOUNT() {
        moduleName = "Account Service";
        displayName = "AccountService";
        outputPath = "accountservice.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }
    class Account {
        String name;
        String type;
        Account (String n, String t) {
            name = n;
            type = t;
        }
    }


    private PackageInfo pkg = null;
    private Vector<Account> accountArray = new Vector<Account>();

    @Override
    public void AddLine (String line) {
        String accounts = "Accounts:";
        System.out.println(line);
        int ind = line.indexOf(accounts);

        String pattern = "Account \\{(.*?)\\}";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
           // logData.add(match.group(1));
            String accountInfo = match.group(1);
            pattern = "name=(.*?), type=(.*)";
            match = Pattern.compile(pattern).matcher(line);
            if (match.find()) {
                String type = match.group(2);
                String name = match.group(1);
                pattern = "com.(.*?)[}.]";
                match = Pattern.compile(pattern).matcher(type);
                Account ac = null;
                if (match.find()) {
                   ac  = new Account(name, match.group(1));
                } else {
                    ac = new Account(name, type);
                }
                accountArray.add(ac);
            }
        }
        logData.add(line);
    }

    @Override
    public ParseTreeNode Parse() {
        String info = " ";
        info += "<br><b>Total Account Numbers :</b> " + accountArray.size();

        for (int i = 0; i < accountArray.size(); i++) {
            info += "<br><b>" + accountArray.get(i).type + " :</b> " + accountArray.get(i).name;
        }
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID,PredefinedNode.BOTTOMPRIORITY, info + "<hr>");
        KeepOrignalOutputWithHtmlTag();
        return treeNode;
    }
}
