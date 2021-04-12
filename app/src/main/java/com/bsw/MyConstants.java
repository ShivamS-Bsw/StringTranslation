package com.bsw;

import java.util.HashMap;
import java.util.Map;

public class MyConstants {

    public static final String CREDENTIAL_PATH = "/credentials.json";
    public static final String SHEET_ID = "1wy2je6dWHetfet942i0ovG-xjrb8H3v2s9CSA-2QT0Q";
    public static final String SHEET_ID_1= "1_Ca-rNQespq-FKc2cdXJ9CL36IOLw72pYq5N855MUHo";
    public static final String stringFilePathDefault = "../ApnaLudoTesting/app/src/main/res/values/strings.xml";

    public static final String APNA_LUDO_SHEET = "Apna Ludo Final";
    public static final String LUDO_SUPERSTAR = "Ludo-final";
    public static final String CALLBREAK = "CB Final";


    public static String getFilePath(String language){
        if (language == null)
            return stringFilePathDefault;
        return String.format("../ApnaLudoTesting/app/src/main/res/values-%s/strings.xml",language);
    }

    public static final Map<String, String> colLangMapApnaLudo = new HashMap<String, String>() {{
            put("A", "en");
            put("B", "hi");
            put("C", "ur");
            put("D", "bn");
            put("E", "ne");
            put("F", "ar");
            put("G", "es");
            put("H", "pt");
            put("I", "in");
            put("J", "fr");
    }};

    public static final Map<String, String> colLangMapLudoSuperstar = new HashMap<String, String>() {{
        put("A", "ar");
        put("B", "bn");
        put("C", "de");
        put("D", "fr");
        put("E", "gu");
        put("F", "hi");
        put("G", "in");
        put("H", "ja");
        put("I", "kn");
        put("J", "ko");
        put("K", "ml");
        put("L", "mr");
        put("M", "nl");
        put("N", "pt");
        put("O", "ru");
        put("P", "ta");
        put("Q", "te");
        put("R", "ur");
        put("S", "zh");
        put("T", "tr");
        put("U", "hi");
        put("W", "es");
    }};
    public static final Map<String, String> colLangMapCallBreak = new HashMap<String, String>() {{
        put("A", "hi");
        put("B", "bn");
        put("C", "de");
        put("D", "en");
        put("E", "es");
        put("F", "fr");
        put("G", "gu");
        put("H", "in");
        put("I", "it");
        put("J", "ja");
        put("K", "ko");
        put("L", "ml");
        put("M", "mr");
        put("N", "pt");
        put("O", "ru");
        put("P", "ta");
        put("Q", "te");
        put("R", "zh");
    }};

    public static void addLanguage(String col, String lang){
        if (col == null || lang == null )
            return;

        colLangMapLudoSuperstar.put(col,lang);
    }

    public static String getSheetNameFromProject(int project){
        switch (project){
            case 1:
                return LUDO_SUPERSTAR;
            case 3:
                return CALLBREAK;
            default:return APNA_LUDO_SHEET; //default 2
        }
    };

    public static final Map<String, String> errorMaps = new HashMap<String, String>() {{
        put("\\N","\\n");
        put("\\ N","\\n");
        put("\\ n","\\n");
        put("%D"," %d");
        put("% D"," %d");
        put("٪ D"," %d");
        put("٪ S"," %s");
        put("% d"," %d");
        put("%S"," %s");
        put("% S"," %s");
        put("% s"," %s");
        put("! [ CDATA [","![CDATA[");
        put("] ] >","]]>");
        put("\\ '"," \\'");
        put("% 1 $ d","%1$d");
        put("% 2 $ d","%2$d");
        put("% 3 $ d","%3$d");
        put("% 4 $ d","%4$d");
        put("% 5 $ d","%5$d");
        put("% 6 $ d","%6$d");
        put("% 7 $ d","%7$d");
        put("% 8 $ d","%8$d");
        put("% 9 $ d","%9$d");
        put("% 1 $ s","%1$s");
        put("% 2 $ s","%2$s");
        put("% 3 $ s","%3$s");
        put("% 4 $ s","%4$s");
        put("% 5 $ s","%5$s");
        put("% 6 $ s","%6$s");
        put("% 7 $ s","%7$s");
        put("% 8 $ s","%8$s");
        put("% 9 $ s","%9$s");
    }};
}
