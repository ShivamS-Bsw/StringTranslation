/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.bsw;

import com.bsw.ui.MyFrame;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class App {

    public static MyFrame myFrame;

    public static void main(String[] args) {
//        myFrame = new MyFrame();
//        GoogleSheetsService.setup();
        validateStrings("<string name=\"shivam\"> Unlocks a9t %s %d %2$s</string>");
    }

    public static void writeLogs(String str) {
        myFrame.writeOutput(str);
    }

    private static void validateStrings(String str) {
        if (validCondition(str)) {
            System.out.println("Valid String" + str);
        } else {
            System.out.println("Invalid String" + str);
        }
    }

    private static boolean validCondition(String str) {
        if (str.startsWith("<string name=") && str.endsWith("</string>")) {

            //Second Validation
            if (str.contains("%")) {
                int len = str.length();
                int i = str.indexOf("%");
                while (i > 0 && i < len && !str.substring(i).equals("</string>")) {

                    if (str.charAt(i) != '%') {
                        i++;
                        continue;
                    }
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
                    } else {
                        i = i + 2;
                    }
                }
                return true;
            }

            //Third Validation
            if (str.contains("&")) {
                int len = str.length();
                int i = str.indexOf("&");

                while (i > 0 && i < len && !str.substring(i).equals("</string>")) {
                    if (str.charAt(i) != '%') {
                        i++;
                        continue;
                    }

                    if (len - i + 1 >= 4 && !(str.charAt(i + 1) == 'a' && str.charAt(i + 2) == 'm' && str.charAt(i + 3) == 'p' && str.charAt(i + 3) == ';')) {
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
                    if (i + 1 < len && !(str.charAt(i + 1) == 'n' || str.charAt(i + 1) == '\'' || str.charAt(i + 1) == '"')) {
                        return false;
                    } else {
                        i = i + 2;
                    }
                }
            }

            //Fifth Validations
            if (str.contains("CDATA")){
                Pattern p = Pattern.compile("(<!\\[CDATA\\[+[\\w\\d\\s<>\\/\"!#=,%$&;'?.]+\\]\\]>)");
                if (!p.matcher(str).find()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
