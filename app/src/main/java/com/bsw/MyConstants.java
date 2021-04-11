package com.bsw;

import java.util.HashMap;
import java.util.Map;

public class MyConstants {

    public static final String CREDENTIAL_PATH = "/credentials.json";
    public static final String SHEET_ID = "1wy2je6dWHetfet942i0ovG-xjrb8H3v2s9CSA-2QT0Q";
    public static final String SHEET_ID_1= "1_Ca-rNQespq-FKc2cdXJ9CL36IOLw72pYq5N855MUHo";
    public static final String stringFilePathDefault = "C:\\Users\\SHIVAM\\Desktop\\apnaLudo\\app\\src\\main\\res\\values\\strings.xml";

    public static final String APNA_LUDO_SHEET = "Apna Ludo Final";
    public static final String LUDO_SUPERSTAR = "Apna Ludo Final";


    public static String getFilePath(String language){
        if (language == null)
            return stringFilePathDefault;
        return String.format("C:\\Users\\SHIVAM\\Desktop\\apnaLudo\\app\\src\\main\\res\\values-%s\\strings.xml",language);
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

    public static void addLanguage(String col, String lang){
        if (col == null || lang == null )
            return;

        colLangMapLudoSuperstar.put(col,lang);
    }

    public static String getSheetNameFromProject(int project){
        switch (project){
            case 1:
                return LUDO_SUPERSTAR;
            default:return APNA_LUDO_SHEET;
        }
    };
}
