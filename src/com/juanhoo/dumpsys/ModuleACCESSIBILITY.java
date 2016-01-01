package com.juanhoo.dumpsys;

import com.juanhoo.Controller.ParseTreeNode;
import com.juanhoo.Controller.ParsefileHandle;
import com.juanhoo.Controller.PredefinedNode;
import com.juanhoo.parse.Module;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/2/2015.
 */
/*
DUMP OF SERVICE accessibility:
User state[attributes:{id=0, currentUser=true, accessibilityEnabled=false, touchExplorationEnabled=false, displayMagnificationEnabled=false}



ser state[attributes:{id=10, currentUser=false, accessibilityEnabled=false, touchExplorationEnabled=false, displayMagnificationEnabled=false}
          services:{}]

indow[AccessibilityWindowInfo[id=274, type=TYPE_SYSTEM, layer=211000, bounds=Rect(0, 2392 - 1440, 2560), focused=false, active=false, hasParent=
alse, hasChildren=false]],
indow[AccessibilityWindowInfo[id=271, type=TYPE_SYSTEM, layer=161000, bounds=Rect(0, 0 - 1440, 84), focused=false, active=false, hasParent=false
 hasChildren=false]],
indow[AccessibilityWindowInfo[id=288, type=TYPE_APPLICATION, layer=21020, bounds=Rect(0, 0 - 1440, 2560), focused=true, active=true, hasParent=f
lse, hasChildren=false]]shell@clark:/ $ dumpsys accessibility
umpsys accessibility
CCESSIBILITY MANAGER (dumpsys accessibility)

ser state[attributes:{id=0, currentUser=true, accessibilityEnabled=true, touchExplorationEnabled=false, displayMagnificationEnabled=false}
          services:{Service[label=Moto Voice, feedbackType[FEEDBACK_AUDIBLE, FEEDBACK_VISUAL, FEEDBACK_GENERIC], capabilities=1, eventTypes=[TYP
_VIEW_CLICKED, TYPE_WINDOW_STATE_CHANGED, TYPE_WINDOW_CONTENT_CHANGED, TYPE_VIEW_SCROLLED], notificationTimeout=50]}]

ser state[attributes:{id=10, currentUser=false, accessibilityEnabled=false, touchExplorationEnabled=false, displayMagnificationEnabled=false}
          services:{}]

indow[AccessibilityWindowInfo[id=274, type=TYPE_SYSTEM, layer=211000, bounds=Rect(0, 2392 - 1440, 2560), focused=false, active=false, hasParent=
alse, hasChildren=false]],
indow[AccessibilityWindowInfo[id=271, type=TYPE_SYSTEM, layer=161000, bounds=Rect(0, 0 - 1440, 84), focused=false, active=false, hasParent=false
 hasChildren=false]],
indow[AccessibilityWindowInfo[id=288, type=TYPE_APPLICATION, layer=21020, bounds=Rect(0, 0 - 1440, 2560), focused=true, active=true, hasParent=f
lse, hasChildren=false]]shell@clark:/ $ dumpsys accessibility
umpsys accessibility
CCESSIBILITY MANAGER (dumpsys accessibility)

ser state[attributes:{id=0, currentUser=true, accessibilityEnabled=true, touchExplorationEnabled=false, displayMagnificationEnabled=false}
          services:{Service[label=Moto Voice, feedbackType[FEEDBACK_AUDIBLE, FEEDBACK_VISUAL, FEEDBACK_GENERIC], capabilities=1, eventTypes=[TYP
_VIEW_CLICKED, TYPE_WINDOW_STATE_CHANGED, TYPE_WINDOW_CONTENT_CHANGED, TYPE_VIEW_SCROLLED], notificationTimeout=50]}]

ser state[attributes:{id=10, currentUser=false, accessibilityEnabled=false, touchExplorationEnabled=false, displayMagnificationEnabled=false}
          services:{}]

 */
public class ModuleACCESSIBILITY extends Module {
    public ModuleACCESSIBILITY() {
        moduleName = "Accessibility Service";
        displayName = "Accessibility Service";
        outputPath = "accessibilityservice.html";
        treeNode = new ParseTreeNode(displayName, outputPath);
    }

    private String patternState = "User state\\[attributes:\\{id=(.*?), currentUser=(.*?), accessibilityEnabled=(.*?), touchExplorationEnabled=(.*?), displayMagnificationEnabled=(.*?)\\}";
    private String patternService = "services:\\{Service\\[label=(.*?), feedbackType";

    HashMap<String, String> accessiblityCapablity = new HashMap<String, String>();


    @Override
    public void AddLine (String line) {
        Matcher match = Pattern.compile(patternState).matcher(line);
        if (match.find()) {
            parseResult.add("<br>User: "+ match.group(1)+ " Accesibiltiy enable: " +match.group(3) + "  Magnification gestures : "+ match.group(5))  ;
        } else {
            match = Pattern.compile(patternService).matcher(line);
            if (match.find()) {
                parseResult.add("<br> " + match.group(1) +" Turn On");

            }
        }
        logData.add(line);

    }

    @Override
    public ParseTreeNode Parse() {
        String info  = "";
        for (int i = 0; i < parseResult.size(); i++) {
            info += parseResult.get(i);
        }
        AppendInfoToFile(ParsefileHandle.OVERVIEWFILEID, PredefinedNode.BOTTOMPRIORITY - 1, info + "<hr>");
        KeepOrignalOutputWithHtmlTag();
        return treeNode;
    }
}
