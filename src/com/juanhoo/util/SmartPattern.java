package com.juanhoo.util;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a20023 on 12/31/2015.
 */
public class SmartPattern {
    String smartPattern;
    Vector<String> memberDef;
    String outputFormat;
    public SmartPattern(String filter) {
        memberDef = new Vector<>();
        GenerateDataFromEnumFile();
        GeneratePattern(filter);
    }

    private void GeneratePattern(String filter){
        String filterPattern = "(.*?)@->(.*?)$";

        Matcher matcher = Pattern.compile(filterPattern).matcher(filter);
        if (matcher.find()) {
            String origPattern = matcher.group(1);
            outputFormat = matcher.group(2);
            String pattern = origPattern;
            pattern = pattern.replace("[", "\\" + "[");
            pattern = pattern.replace("]", "\\" + "]");
            pattern = pattern.replace(")", "\\" + ")");
            pattern = pattern.replace("(", "\\" + "(");
            pattern = pattern.replace("{", "\\" + "{");
            pattern = pattern.replace("}", "\\" + "}");
            pattern = pattern.replaceAll("%%\\w++%%", "(.*?)");
            matcher = Pattern.compile(pattern).matcher(origPattern);
            if (matcher.find()) {
                for (int i = 0; i <= matcher.groupCount(); i++) {
                    memberDef.add(i, matcher.group(i).replaceAll("%%", ""));
                }
            }
            pattern = "(^[\\d-]{5}) ([\\d:.]{12})  ([\\d]{4})  ([\\d]{4})(.*?)" + pattern;
            smartPattern = pattern;
        } else {
            System.out.println("Generate pattern failed: "+ filter);
        }
    }

   // private String[] testNum = {"test0","test1","test2","test3","test4","test5","test6","test7","test8","test9","test10","test11","test12","test13","test14","test15","test16","test17"};


    public String ParseLine(String line) {
        if (outputFormat == null) {
            return null;
        }
        Matcher matcher = Pattern.compile(smartPattern).matcher(line);
        if (matcher.find()) {
            String output = matcher.group(1) + " " +matcher.group(2)+ " ";
            int charIndex = 0;
            int formatLength = outputFormat.length();
            while (charIndex < formatLength) {
                char c = outputFormat.charAt(charIndex);
                if (c != '$') {
                    output += c;
                    charIndex++;
                } else {
                    String numberStr = "";
                    charIndex++;
                    while (true) {
                        c = outputFormat.charAt(charIndex);
                        if (c <= '9' && c >= '0') {
                            numberStr += c;
                            charIndex++;
                            if (charIndex == formatLength) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (numberStr.length() > 0) {
                        int number = Integer.parseInt(numberStr);
                        output += " " + GetEnumNameByValue(memberDef.get(number), matcher.group(number+5));
                    }
                }
            }
            return output;

        }
        return null;
    }

    private static HashMap<String, HashMap<String, String>> enumVarMap = new HashMap<>();
    private static HashMap<String, String> enumValueMap;
    private static boolean isEnumCreated = false;

    private void GenerateDataFromEnumFile() {

        if (isEnumCreated) {
            return;
        }

        String fileName = "parammeaning.txt";
        File file = new File (fileName);
        String newSection = "typedef enum {";
        if (!file.exists()) {
            System.out.println("Cannot find file " + fileName);
        }

        String enumValueNamePattern = "\\s++(.*?)\\s++=\\s++(\\d{1,4}+).*?";
        String enumVariableNamePattern = "\\s*?}\\s*?(\\S*?);\\s*?";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(newSection)) {
                    enumValueMap = new HashMap<>();
                    continue;
                }
                Matcher match = Pattern.compile(enumVariableNamePattern).matcher(line);
                if (match.find()) {
                    enumVarMap.put(match.group(1), enumValueMap);
                    continue;
                }
                match = Pattern.compile(enumValueNamePattern).matcher(line);
                if (match.find()) {
                    enumValueMap.put(match.group(2), match.group(1));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isEnumCreated = true;
    }

    String GetEnumNameByValue(String varName, String value) {
        if (varName.equalsIgnoreCase("keepOrignal")) {
            return value;
        }
        HashMap<String,String> enumNameValueMap = enumVarMap.get(varName);
        if (enumNameValueMap != null) {
            return enumNameValueMap.get(value);
        }
        return null;
    }

}
