package com.juanhoo.dumpservice;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.parse.Module;

import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/21/2015.
 */
public class ModuleSettingsProvider extends Module {
    public ModuleSettingsProvider() {
        moduleName = "Settings Provider";
        displayName = "Settings Provider";
        outputPath = "settingsprovider.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }

    Vector<String> infoVector = new Vector<>();
    {
        for (int i = 0; i < ParsefileHandle.MAXPREDEFINEDFILEID+1; i++) {
            infoVector.add("");
        }
    }


    private String audioInfo = "";
    protected String userId = null;
    protected String settingType = null;

    abstract class RecordParser {
        int fileID;
        String matchPattern;
        String recordDescription;

        RecordParser(int id, String pattern, String description) {
            fileID = id;
            recordDescription = description;
            matchPattern = pattern;
        }

        abstract boolean Parse(String line);
    }

    class SwitchRecordParser extends  RecordParser{

        SwitchRecordParser(int id, String pattern, String description) {
            super(id, pattern, description);
        }

        boolean Parse(String line) {
            Matcher match  = Pattern.compile(matchPattern).matcher(line);
            if (match.find()) {
            /*    AppendInfoToFile(fileID,"<br>" + recordDescription +
                        (match.group(3).equals("1")?"ON":"OFF") +"<hr>");*/
                String info = infoVector.get(fileID);
                info = info + "<hr><br>" + "<b>User "+userId+" :</b>"+recordDescription +
                        (match.group(3).equals("1")?"ON":"OFF") ;
                infoVector.set(fileID, info);
                return true;
            }
            return false;
        }
    }

    ArrayList<RecordParser> recordList = new ArrayList<>();
    {
        recordList.add(new SwitchRecordParser(ParsefileHandle.CONNECTIVITYFILEID,
                "^(.*?)bluetooth_on([\\| value:]*?)([0-9]{1})",
                "Bluetooth Status: "));
        recordList.add(new SwitchRecordParser(ParsefileHandle.CONNECTIVITYFILEID,
                "^(.*?)wifi_on([\\| value:]*?)([0-9]{1})",
                "WIFI Status: "));
        recordList.add(new SwitchRecordParser(ParsefileHandle.DATACONNFILEID,
                "^(.*?)mobile_data([\\| value:]*?)([0-9]{1})",
                "Cellular data: "));
        recordList.add(new SwitchRecordParser(ParsefileHandle.DATACONNFILEID,
                "^(.*?)data_roaming([\\| value:]*?)([0-9]{1})",
                "Data Roaming: "));
        recordList.add(new SwitchRecordParser(ParsefileHandle.NETWORKFILEID,
                "^(.*?)airplane_mode_on([\\| value:]*?)([0-9]{1})",
                "Airplane Mode : "));
        recordList.add(new SwitchRecordParser(ParsefileHandle.NETWORKFILEID,
                "^(.*?)volte_vt_enabled([\\| value:]*?)([0-9]{1})",
                "Volte/VT : "));

    }

    private boolean GetUserID(String line) {
        String pattern = "^\\s++(\\w+?) SETTINGS \\(user (.*?)\\)";
        Matcher match = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            settingType = match.group(1);
            userId = match.group(2);
            return true;
        }
        return false;
    }


    @Override
    public void AddLine(String line) {
        super.AddLine(line);
        String pattern = "^(.*?)volume_(.*?)([\\| value:]*?)([0-9]{1,2})$";
        Matcher match  = Pattern.compile(pattern).matcher(line);
        if (match.find()) {
            audioInfo += "<br>" + "<b>User "+userId+" :</b>"+match.group(2)+" volume: "+ match.group(4);
            return;
        }

        if (GetUserID(line)) {
            return;
        }

        for (int i  = 0; i < recordList.size(); i++) {
            if (recordList.get(i).Parse(line) == true) {
                break;
            }
        }
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.AUDIOFILEID, "<br><hr><b>Audio Volume Setting </b><hr>"+audioInfo+"<hr>");
        for (int fileId = 0; fileId < ParsefileHandle.MAXPREDEFINEDFILEID+1; fileId++) {
            AppendInfoToFile(fileId, infoVector.get(fileId));
        }
        KeepOrignalOutputWithHtmlTag();
        return treeNode;
    }
}
