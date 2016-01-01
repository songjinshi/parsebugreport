package com.juanhoo.parse;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.util.SmartPattern;

/**
 * Created by a20023 on 11/3/2015.
 */

//12-28 22:04:38.187  2857  2926 D RILJ    : [3718]< VOICE_REGISTRATION_STATE {12, null, null, 0, null, null, null, 0, null, null, null, null, null, 0, 63} [SUB0]
//< VOICE_REGISTRATION_STATE {%%getVoiceRegState%%, %%keepOrignal%%, %%keepOrignal%%, %%getRilRadioTech%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%} [SUB%%keepOrignal%%]@->Voice registration State(SIM $16 ):$1 ,LAC:$2 ,CID:$3 ,Radio tech:$4 ,Base station:$5 ,System ID:$9 ,Network ID:$10, In PRL:$12 {color}:Gray
public class ModuleRADIOLOG extends ModuleFileLog {
    public ModuleRADIOLOG() {
        moduleName = "Radio_log";
        outputPath = "radio_log.txt";
        ParsefileHandle.AddFileToMergeList(ParsefileHandle.OUTPUTDIR+"\\"+outputPath);

    }

    SmartPattern sp = new SmartPattern("< VOICE_REGISTRATION_STATE {%%REGSTATE%%, %%keepOrignal%%, %%keepOrignal%%, %%RIL_RadioTechnology%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%, %%keepOrignal%%} [SUB%%keepOrignal%%]@->Voice registration State(SIM $16 ):$1 ,LAC:$2 ,CID:$3 ,Radio tech:$4 ,Base station:$5 ,System ID:$9 ,Network ID:$10, In PRL:$12 ");
    String info = "";

    @Override
    public void AddLine(String line) {
        super.AddLine(line);
        String parseReulst = sp.ParseLine(line);
        if (parseReulst != null) {
            info += parseReulst;
        }
    }

    @Override
    public ParseTreeNode Parse() {
        AppendInfoToFile(ParsefileHandle.NETWORKFILEID, info);
        return super.Parse();
    }
}
