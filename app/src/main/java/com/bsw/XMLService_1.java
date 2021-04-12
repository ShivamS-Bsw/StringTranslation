package com.bsw;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
    private BufferedReader reader;
    private StringBuilder resultStringBuilder;
    private String filePath;

    XMLService_1(String language) {
        resultStringBuilder = new StringBuilder();
        this.language = language;
        this.filePath = MyConstants.getFilePath(language);
        readFile();
    }

    private void readFile() {

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendFile(String str) {

        for (String key : MyConstants.errorMaps.keySet()) {
            if (str.contains(key)){
                str = str.replace(key, MyConstants.errorMaps.get(key));
            }
        }

        if (resultStringBuilder == null || resultStringBuilder.toString().trim().isEmpty()) {
            System.out.println("Write Failed: " + language + " :" + str);
            return;
        }

        int lastIndexOf = resultStringBuilder.lastIndexOf("</resources>");
        resultStringBuilder.insert(lastIndexOf - 1, "\n" + str);
        System.out.println("Write Success: " + language + " :" + str);

    }

    private void closeFile() {
        try {
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
