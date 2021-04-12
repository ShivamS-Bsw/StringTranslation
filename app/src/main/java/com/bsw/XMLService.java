package com.bsw;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
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

public class XMLService implements AutoCloseable{

    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder documentBuilder;
    private Document file = null;
    private String language;

    XMLService(String language){
        try {
            this.language = language;
            documentBuilder = factory.newDocumentBuilder();
            readFile();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void readFile() {
        try {
            file = documentBuilder.parse(new File(MyConstants.getFilePath(language)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void appendFile(String str) {
        try {

            for (String key : MyConstants.errorMaps.keySet()){
                str = str.replace(key,MyConstants.errorMaps.get(key));
            }

            Document element = null;
            element = documentBuilder.parse(new ByteArrayInputStream(str.getBytes(Charset.forName("UTF-8"))));
            Node importedNode = file.importNode(element.getFirstChild(), true);
            Element root = file.getDocumentElement();
            if (importedNode != null) {
                root.appendChild(importedNode);
            }
            System.out.println("Write Success: " + language + " :" + str);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeFile() {

        Transformer tr = null;
        try {
            tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(new DOMSource(file), new StreamResult(new OutputStreamWriter(new FileOutputStream(new File(MyConstants.getFilePath(this.language))), StandardCharsets.UTF_8)));

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        System.out.println("Closing File : " + language);
    }

    @Override
    public void close() throws Exception {
        closeFile();
    }
}
