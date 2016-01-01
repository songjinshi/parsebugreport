package com.juanhoo.Controller;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 11/5/2015.
 */
public class PackageInfo {
    String packageName;
    int packageUID;
    String codePath;
    String versionCode;
    int targetSDK;
    String versionName;
    ArrayList<String> permission;

    public void SetPackageName (String name) {
        packageName = name;
    }
    HashMap<String, String> packageDetailMap = new HashMap<String, String>();
    LinkedList<String> keys = new LinkedList<String>();
    String[] keyWordList = {"userId", "codePath", "versionName", "flags", "firstInstallTime", "lastUpdateTime", "installerPackageName"};

    public PackageInfo() {
        for (int i = 0; i < keyWordList.length; i++) {
            keys.add(keyWordList[i]);
        }
    }

    @Override
    public String toString() {
        String result = "<b>"+packageName+"</b>\n";
        for (int i = 0; i < keyWordList.length; i++) {
            String key = keyWordList[i];
            result = result + "<br> "+ key+ " "+ packageDetailMap.get(key);
        }
        return result;
    }

    //Pattern.compile("Bootloader: (.*?)$")
    public void ParseData(String line) {
        if (keys.size() == 0) {
            return;
        }
        ListIterator<String> it = keys.listIterator();

        while (it.hasNext()) {
            String key = it.next();
            String pattern = key+"=(.*?)$";
            Matcher match = Pattern.compile(pattern).matcher(line);
            if (match.find()) {
                packageDetailMap.put(key, match.group(1));
                it.remove();
            }
        }
    }


}
