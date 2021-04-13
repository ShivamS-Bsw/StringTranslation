package com.bsw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CompareService {

    /*
    * hash1 : Old String Set
    * hash2 : New String Set
    *
    * */
    public static void compareFileContents(HashSet<String> hash1, HashSet<String> hash2, String str){

        HashSet <String> diff1 = new HashSet<String>();
        HashSet <String> diff2 = new HashSet<String>();

        for (String str3 : hash1) {
            if (!hash2.contains(str3)) {
                diff1.add(str3);
            }
        }
        for (String str4 : hash2) {
            if (!hash1.contains(str4)) {
                diff2.add(str4);
            }
        }
        //Difference 1 = Removed
        //Difference 2 = Added

        StringBuilder resultStringBuilder = new StringBuilder();

        resultStringBuilder.append("<version " + "1.0>").append("\n\n\n");

        if (diff1.size() == 0 && diff2.size() == 0){
            resultStringBuilder.append("No Changes");
        }else{
            resultStringBuilder.append("Removed:\n");
            for(String i : diff1){
                resultStringBuilder.append(i).append("\n");
            }

            resultStringBuilder.append("\n\n\n");
            resultStringBuilder.append("Added:\n");
            for(String i : diff1){
                resultStringBuilder.append(i).append("\n");
            }
        }
        writeFile(str);
    }

    private static void writeFile(String res){
        try {
            FileWriter fw= null;
            fw = new FileWriter("file.txt");
            fw.write(res);
            fw.close();
            System.out.println("File Written Success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
