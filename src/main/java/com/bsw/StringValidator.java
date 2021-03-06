package com.bsw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class StringValidator {

    private StringBuilder invalidStrings;
    private List<List<Object>> colList;

    StringValidator( List<List<Object>> colList){
        invalidStrings = new StringBuilder();
        this.colList = colList;
    }

    public boolean checkForEachStrings() {

        for (List<Object> objects : this.colList) {

            if (objects == null || objects.size() == 0)
                continue;

            String lang = objects.get(0).toString();
            for (int i = 1; i < objects.size(); i++) {
                String data = objects.get(i).toString().trim();
                if (!data.isEmpty() && !validCondition(data)){
                    appendToStrings(data,lang,i);
                }
            }
        }

        writeInvalidStringToFile();

        if(invalidStrings != null){
            if (invalidStrings.toString().trim().isEmpty()){
                return false;
            }
        }
        return true;
    }

    private void appendToStrings(String data, String lang, int rowNumber){
        this.invalidStrings.append("Invalid String at ").append(lang).append("  Row:").append(rowNumber + "  ").append(data).append("\n");
    }

    private void writeInvalidStringToFile(){

        try {
            String fileName = "invalid.txt";
            FileWriter writer = new FileWriter(new File("invalid.txt"));
            writer.write(this.invalidStrings.toString());
            writer.flush();
            writer.close();
            if (!this.invalidStrings.toString().isEmpty()) {
                App.writeLogs("Invalid Strings Found: Write Success -" + fileName);
            }
        } catch (IOException e) {
            App.writeLogs("Failed to write invalid strings:" + e.getMessage());
        }

    }

    private static final Pattern p1 = Pattern.compile("<!--[\\s\\S].*?-->"); // Comments in xml tags
    private static final Pattern p2 = Pattern.compile("&lt;br\\s*\\/?&gt;"); // Comments in br tags
    private static final Pattern p3 = Pattern.compile("<!\\[CDATA\\[.*?\\]\\]>"); // CDATA

    public static boolean validCondition(String str) {

        if (p1.matcher(str).find()){
            str = p1.matcher(str).replaceAll("").trim();

            if(str.isEmpty())
                return true;
        }

        if (!str.isEmpty() && (str.startsWith("<string ") && str.endsWith("</string>")) || (str.startsWith("<item>") && str.endsWith("</item>")) || str.endsWith("</string-array>") || str.startsWith("<string-array name=")) {


            // Converting the valid CDATA with "" because inside CDATA can have & not &amp;
            if (str.contains("CDATA")){
                if (p3.matcher(str).find()){
                    str = p3.matcher(str).replaceAll("").trim();
                }else
                    return false;
            }

            // Converting the valid br tags with "" &lt;br\\
            if (str.contains("br /") || str.contains("br/")){
                if (p2.matcher(str).find()){
                    str = p2.matcher(str).replaceAll("").trim();
                }else
                    return false;
            }


            if (str.contains("??"))
                return false;

            //Second Validation
            if (str.contains("%")) {
                int len = str.length();
                int i = str.indexOf("%");
                while (i >= 0 && i < len && !str.substring(i).equals("</string>")) {

                    if (str.charAt(i) != '%') {
                        i++;
                        continue;
                    }

                    if (i > 0 && str.charAt(i-1) >= 48 && str.charAt(i-1) <= 57) { //% is precedded with numbers
                        i++;
                        continue;
                    }

                    // %s || %d || %f
                    if (i + 1 < len && !(str.charAt(i + 1) == 's' || str.charAt(i + 1) == 'd' || str.charAt(i + 1) == 'f')) {

                        if (len - i + 1 >= 3 && !(str.charAt(i + 1) >= 33 && str.charAt(i + 1) <= 41)) {
                            if (str.charAt(i + 2) == '$') {
                                if (!(str.charAt(i + 3) == 's' || str.charAt(i + 3) == 'd' || str.charAt(i + 3) == 'f')) {
                                    return false;
                                } else {
                                    i = i + 4;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                        //%s%%
                    } else if (i + 3 < len && ((str.charAt(i + 1) == 's' || str.charAt(i + 1) == 'd' || str.charAt(i + 1) == 'f') && str.charAt(i + 2) == '%' && str.charAt(i + 3) == '%')){
                        i = i + 4;
                    }else {
                        i = i + 2;
                    }
                }
            }

            //Third Validation
            if (str.contains("&") && !str.contains("&quot;") && !str.contains("&#8230;")) {
                int len = str.length();
                int i = str.indexOf("&");

                while (i > 0 && i < len && !str.substring(i).equals("</string>")) {
                    if (str.charAt(i) != '&') {
                        i++;
                        continue;
                    }

                    if (len - i + 1 >= 4 && !(str.charAt(i + 1) == 'a' && str.charAt(i + 2) == 'm' && str.charAt(i + 3) == 'p' && str.charAt(i + 4) == ';')) {
                        return false;
                    } else {
                        i = i + 5;
                    }
                }
            }

            //Fourth Validation
            if (str.contains("\\")) {

                int len = str.length();
                int i = str.indexOf("\\");

                while (i > 0 && i < len && !str.substring(i).equals("</string>")) {

                    if (str.charAt(i) != '\\') {
                        i++;
                        continue;
                    }
                    if (i + 1 < len && !(str.charAt(i + 1) == 'n' || str.charAt(i + 1) == 't' || str.charAt(i + 1) == '?' || str.charAt(i + 1) == 'u' || str.charAt(i + 1) == '\'' || str.charAt(i + 1) == '"')) {
                        return false;
                    } else {
                        i = i + 2;
                    }
                }
            }

            return true;
        }
        return false;
    }
}
