package com.bsw;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLService_1 implements AutoCloseable{

    private String language;
    private StringBuilder resultStringBuilder;
    private String filePath;
    private HashSet<String> str1;
    private HashSet<String> str2;

    XMLService_1(String language) throws FileNotFoundException {
        str1 = new HashSet<String>();
        str2 = new HashSet<String>();
        resultStringBuilder = new StringBuilder();
        this.language = language;
        this.filePath = MyConstants.getFilePath(this.language);

        if (this.language.equals("") || this.language.equals("default")){
            this.filePath = MyConstants.getFilePath(this.language);
        }

        addStartingString();

        if (this.language.equals("en")){
//            readFile();
        }
    }

    private void readFile() throws FileNotFoundException{
        System.out.println("Opening File : " + language);
            FileInputStream fis= null;
            fis = new FileInputStream(new File(filePath));
            Scanner sc=new Scanner(fis);
            while(sc.hasNextLine()) {
                if (!sc.nextLine().trim().isEmpty()){
                    str1.add(sc.nextLine());
                }
            }
            sc.close();
    }

    private void addStartingString(){
        resultStringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n");
        resultStringBuilder.append("<resources>").append("\n");

        str2.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        str2.add("<resources>");
    }

    private void addEndingString(){
        resultStringBuilder.append("</resources>").append("\n");
        str2.add("</resources>");
    }

    public void appendFile(String str) {

        str2.add(str);

        for (String key : MyConstants.errorMaps.keySet()) {
            if (str.contains(key)){
                str = str.replace(key, MyConstants.errorMaps.get(key));
            }
        }

        if (resultStringBuilder == null) {
            resultStringBuilder = new StringBuilder();
            addStartingString();
        }

        resultStringBuilder.append(str).append("\n");

    }

    private void closeFile() {
        try {
            addEndingString();
            FileWriter writer = new FileWriter(new File(filePath));
            writer.write(resultStringBuilder.toString());
            writer.flush();
            writer.close();
            System.out.println("Closing File : " + language);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        closeFile();
    }
}
