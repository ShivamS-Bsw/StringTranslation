package com.bsw;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyConstants {

    public static final String CREDENTIAL_PATH = "/credentials.json";
    public static String SHEET_ID = "";
    public static final String VERSION_SHEET_NAME= "Version"; //Version Sheet Name
    public static String stringFilePathDefault = "";
    public static String stringFilePathLang = "";
    public static int currentVersion = 0;
    public static String MASTER_SHEET = "";

    public static String getFilePath(String language){
        if (language == null || language.equals("default"))
            return stringFilePathDefault;
        return String.format(stringFilePathLang,language);
    }

    public static boolean setFilePath(String path){

        if (path.trim().isEmpty())
            return false;

        if (path.trim().charAt(path.length()-1) == File.separatorChar){
            path = path.substring(0, path.length() - 1);
        }

        stringFilePathDefault = path.trim() + File.separator +  "values" + File.separator + "strings.xml";
        stringFilePathLang = path.trim() + File.separator +  "values-%s" + File.separator + "strings.xml";
        return true;
    }

    public static final Map<String, String> errorMaps = new HashMap<String, String>() {{
        put("\\N","\\n");
        put("\\ N","\\n");
        put("\\ n","\\n");
        put("%D"," %d");
        put("% D"," %d");
        put("٪ D"," %d");
        put("٪ d"," %d");
        put("٪ S"," %s");
        put("٪ s"," %s");
        put("% d"," %d");
        put("%S"," %s");
        put("% S"," %s");
        put("% s"," %s");
        put("! [ CDATA [","![CDATA[");
        put("] ] >","]]>");
        put("٪"," %");
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

    public static Integer getRandomSheetId(){
        return new Random().nextInt(9999);
    }
    public static void incrementVersion(){
        MyConstants.currentVersion++;
    }
//    private void setMasterSheetName(int project){
//        MASTER_SHEET = projectSheetMapName.get(project);
//    }

    public static final SortedMap<String,String> projectName = new TreeMap<String,String>(){{
        put("Ludo","Ludo-master");
        put("Callbreak","CB final");
        put("Apna Ludo","Apna Ludo Final");

    }};

    public static final SortedMap<String,String> projectSheetIdMapName = new TreeMap<String,String>(){{
        put("Ludo","1fZ6OW329nKV4mF0Ri_x37XSFYCEDqZSpGgu8xiosfQo");
        put("Callbreak", "1_Ca-rNQespq-FKc2cdXJ9CL36IOLw72pYq5N855MUHo");
        put("Apna Ludo", "1_Ca-rNQespq-FKc2cdXJ9CL36IOLw72pYq5N855MUHo");

    }};

    public static String[] getProjectName(){
        String[] str = new String[projectName.size()];
        int i = 0;
        for (String s : projectName.keySet()){
            str[i] = s;
            i++;
        }

        return str;
    }
}
