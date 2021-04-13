package com.bsw;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyConstants {

    public static final String CREDENTIAL_PATH = "/credentials.json";
    public static final String SHEET_ID = "1b9-Aygfad2JsTxURYuFZH0_fsUIPztRxgcmDZWBxU6w";
    public static final String SHEET_ID_1= "1_Ca-rNQespq-FKc2cdXJ9CL36IOLw72pYq5N855MUHo"; // BSW Translation
    public static final String SHEET_ID_2= "1ghguW5BTWqftDWMgDXBafobWoCZuAqRbxDwNx6j2gOw"; //New Translation
    public static final String SHEET_ID_3= "1fZ6OW329nKV4mF0Ri_x37XSFYCEDqZSpGgu8xiosfQo"; //New Translation
    public static final String VERSION_FILE_SHEET_ID= "1hz_0Lun6jF7ZCDRbqqFFKG9nzVCJVm-CruKb0VnNvPg"; //New Translation
    public static String stringFilePathDefault = "";
    public static double currentVersion = 1.0;

    public static final String APNA_LUDO_SHEET = "Apna Ludo Final";
    public static final String LUDO_SUPERSTAR = "Ludo-master";
    public static final String CALLBREAK = "CB Final";
    public static final String MASTER_SHEET = "";

//    public static String getFilePath(String language){
//        if (language == null || language.equals("default"))
//            return "../LudoAndroidMaster/app/src/allLang/res/values/strings.xml";
//        return String.format("../LudoAndroidMaster/app/src/allLang/res/values-%s/strings.xml",language);
//    }
    public static String getFilePath(String language){
        if (language == null || language.equals("default"))
            return String.format("C:\\Users\\SHIVAM\\Desktop\\apnaLudo\\app\\src\\main\\res\\values\\strings.xml",language);
        return String.format("C:\\Users\\SHIVAM\\Desktop\\apnaLudo\\app\\src\\main\\res\\values-%s\\strings.xml",language);
    }

    public static boolean setFilePath(String path){

        if (path.trim().isEmpty())
            return false;

        if (path.trim().charAt(path.length()) == File.separatorChar){
            path = path.substring(0, path.length() - 1);
        }

        stringFilePathDefault = path.trim() + File.separator +  "values-%s" + File.separator + "strings.xml";
        return true;
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
        put("stringname","string name");
        put("& amp;","&amp;");
        put("\\'","|!|QUOTE|!|");
        put("\\ '","|!|QUOTE|!|");
        put("'","|!|QUOTE|!|");
        put("|!|QUOTE|!|","\\'");
        put("<\\","</");
        put("& A","&amp;");
        put("& amp ;","&amp;");
        put("& amp","&amp;");
    }};
}
