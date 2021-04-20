package com.bsw;

import com.bsw.ui.MyFrame;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DuplicateSheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class GoogleSheetsService {
    private static Sheets sheetsService;
    private static String SPREADSHEET_ID = MyConstants.SHEET_ID;
    private static SheetProperties masterSheetProperties;
    private static long max = Long.MAX_VALUE;
    private StringBuilder resultStringBuilder;

    public static void setup() {
        try {
            sheetsService = SheetsServiceUtil.getSheetsService();
            getSpreadSheetPropertiesData();
        } catch (Exception e) {
            App.writeLogs(e.getMessage());
        }
    }

    public static void readFromSpreadSheet() {

        try {
            if (sheetsService == null) {
                setup();
            }

            List<String> ranges = new ArrayList<>();
            ranges.add(MyConstants.MASTER_SHEET);

            String valueRenderingOptions = "FORMATTED_VALUE";

            BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                    .batchGet(SPREADSHEET_ID)
                    .setRanges(ranges)
                    .setValueRenderOption(valueRenderingOptions)
                    .setMajorDimension("COLUMNS")
                    .execute();

            List<ValueRange> valueRanges = readResult.getValueRanges();
            for (ValueRange valueRange : valueRanges) {
                List<List<Object>> colList = valueRange.getValues();
                boolean invalidStringsFound = new StringValidator(colList).checkForEachStrings();

                if (!invalidStringsFound) {
                    for (List<Object> objects : colList) {
//
                        if (objects == null || objects.size() == 0)
                            continue;

                        String lang = objects.get(0).toString(); // Lang Coloumn
                        max = max - objects.size();
                        try (XMLService_1 xmlService_1 = new XMLService_1(lang)) {
                            for (int i = 1; i < objects.size(); i++) {
                                String data = objects.get(i).toString();

                                if (checkForValidData(data)) {
                                    xmlService_1.appendFile(data.trim());
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    checkIfNewDataHasAdded();
                }
            }
        } catch (Exception e) {
            App.writeLogs(e.getMessage());
            App.writeLogs("Stopping Execution");
        }
    }

    public static void readFromSpreadSheetForSingleLang(int project, String l) throws IOException, GeneralSecurityException {

        if (sheetsService == null) {
            setup();
        }

        if (l == null || l.trim().isEmpty())
            return;

        List<String> ranges = new ArrayList<>();
        ranges.add(MyConstants.MASTER_SHEET);

        String valueRenderingOptions = "FORMATTED_VALUE";

        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .setValueRenderOption(valueRenderingOptions)
                .setMajorDimension("COLUMNS")
                .execute();

        List<ValueRange> valueRanges = readResult.getValueRanges();
        for (ValueRange valueRange : valueRanges) {
            List<List<Object>> colList = valueRange.getValues();


//            for (List<Object> objects : colList){
//
//                if (objects == null || objects.size() == 0)
//                    continue;
//                String lang = objects.get(0).toString(); // Lang Coloumn
//                max = max - objects.size();
//                if (lang.equals(l)){
//                    try (XMLService_1 xmlService_1 = new XMLService_1(lang)) {
//                        for (int i = 1; i < objects.size(); i++) {
//                            String data = objects.get(i).toString();
//
//                            if (checkForValidData(data)){
//                                xmlService_1.appendFile(data.trim());
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
            //Something has written to that file
//            checkIfNewDataHasAdded();
        }
    }

    private static boolean validCondition(String str) {
        if (str.startsWith("<string name=") && str.endsWith("</string>")) {

            if (str.contains("٪"))
                return false;

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
            if (str.contains("CDATA")) {
                Pattern p = Pattern.compile("(<!\\[CDATA\\[+[\\w\\d\\s<>\\/\"!#=,%$&;'?.]+\\]\\]>)");
                if (!p.matcher(str).find()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    private static void checkIfNewDataHasAdded() throws GeneralSecurityException, IOException {
        if (max != Long.MAX_VALUE) {
            saveLogs();
        }
    }

    private static boolean checkForValidData(String data) {

        return !data.trim().isEmpty();

    }

    private static int containsVersionSheet = -1; // -1 Not Present 0 Created 1 Already Present
    private static boolean containsMasterSheet = false; // -1 Not Present 0 Created 1 Already Present
    private static boolean isVersionSheetCreated = false;

    public static void getSpreadSheetPropertiesData() {

        try {

            containsMasterSheet = false;

            if (sheetsService == null) {
                setup();
            }

            Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(SPREADSHEET_ID);
            Spreadsheet response = request.execute();

            if (response == null) {
                throw new Exception("Failed to fetch properties from the spreadsheetId");
            }

            List<Sheet> sheetsList = response.getSheets();

            for (Sheet sheet : sheetsList) {

                if (sheet.getProperties().getTitle().equals(MyConstants.VERSION_SHEET_NAME) && containsVersionSheet == -1) {
                    containsVersionSheet = 1;
                }

                if (sheet.getProperties().getTitle().equals(MyConstants.MASTER_SHEET)) {
                    containsMasterSheet = true;
                    masterSheetProperties = sheet.getProperties();
                    break;
                }
            }

            if (!containsMasterSheet) {
                throw new Exception("Sheet not present. Please check the sheet name");
            }

            if (containsVersionSheet == -1) {
                createVersionSheet();
            } else if (containsVersionSheet == 1) {
                readFromVersionSheet();
            }

        } catch (Exception e) {
            App.writeLogs(e.getMessage());
        }
    }

    public static void createVersionSheet() {
        try {

            if (sheetsService == null) {
                setup();
            }

            if (masterSheetProperties == null) {
                getSpreadSheetPropertiesData();
            }

            List<Request> requests = new ArrayList<>();
            requests.add(new Request()
                    .setAddSheet(
                            new AddSheetRequest()
                                    .setProperties(new SheetProperties()
                                            .setTitle(MyConstants.VERSION_SHEET_NAME)
                                            .setIndex(0)
                                            .setSheetId(MyConstants.getRandomSheetId())
                                    )
                    )
            );
            BatchUpdateSpreadsheetRequest body =
                    new BatchUpdateSpreadsheetRequest()
                            .setRequests(requests);
            BatchUpdateSpreadsheetResponse response =
                    sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
            isVersionSheetCreated = true;
            readFromVersionSheet();

        } catch (Exception e) {
            isVersionSheetCreated = false;
            App.writeLogs(e.getMessage());
            App.writeLogs("Version sheet creation failed");
        }
    }

    public static void saveLogs() {

        try {

            if (sheetsService == null) {
                setup();
            }

            if (masterSheetProperties == null) {
                getSpreadSheetPropertiesData();
            }

            List<Request> requests = new ArrayList<>();
            requests.add(new Request()
                    .setDuplicateSheet(
                            new DuplicateSheetRequest()
                                    .setNewSheetName(generateNewSheetName())
                                    .setSourceSheetId(masterSheetProperties.getSheetId())
                                    .setNewSheetId(MyConstants.getRandomSheetId())
                                    .setInsertSheetIndex(isVersionSheetCreated ? masterSheetProperties.getIndex() + 1 + 1 : masterSheetProperties.getIndex() + 1)
                    )
            );
            BatchUpdateSpreadsheetRequest body =
                    new BatchUpdateSpreadsheetRequest()
                            .setRequests(requests);
            BatchUpdateSpreadsheetResponse response =
                    sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();

            App.writeLogs("Sheet Creation Success");
            writeToVersionSheet();
        } catch (Exception e) {
            App.writeLogs(e.getMessage());
            App.writeLogs("Sheet Creation Failed");
        }
    }

    public static void readFromVersionSheet() {

        try {
            if (sheetsService == null) {
                setup();
            }

            List<String> ranges = new ArrayList<>();
            ranges.add(MyConstants.VERSION_SHEET_NAME);

            String valueRenderingOptions = "FORMATTED_VALUE";

            BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                    .batchGet(SPREADSHEET_ID)
                    .setRanges(ranges)
                    .setValueRenderOption(valueRenderingOptions)
                    .setMajorDimension("ROWS")
                    .execute();

            List<ValueRange> valueRanges = readResult.getValueRanges();
            for (ValueRange valueRange : valueRanges) {
                List<List<Object>> colList = valueRange.getValues();

                if (colList != null) {
                    for (int i = colList.size() - 1; i >= 0; i--) {

                        if (colList.get(i) != null && colList.get(i).get(2) != null) {

                            if (colList.get(i).get(2).toString().trim().equals(MyConstants.MASTER_SHEET)) {
                                MyConstants.currentVersion = Integer.parseInt(colList.get(i).get(3).toString());
                                break;
                            }
                        }
                    }
                }
            }
            MyConstants.incrementVersion();
            readFromSpreadSheet();
        } catch (Exception e) {
            App.writeLogs(e.getMessage());
        }
    }

    private static void writeToVersionSheet() {
        try {
            List<List<Object>> values = Arrays.asList(
                    Arrays.asList(
                            getCurrentTimeStamp(),
                            SPREADSHEET_ID,
                            masterSheetProperties.getTitle(),
                            MyConstants.currentVersion
                    )
                    // Additional rows ...
            );

            ValueRange body = new ValueRange()
                    .setValues(values);

            AppendValuesResponse result =
                    sheetsService.spreadsheets().values().append(SPREADSHEET_ID, MyConstants.VERSION_SHEET_NAME, body)
                            .setValueInputOption("RAW")
                            .execute();
            App.writeLogs("Logging Success");

        } catch (Exception e) {
            App.writeLogs("Logging Failed");
        }
    }

    private static String getCurrentTimeStamp() {
        return new Date().toString();
    }

    private static String generateNewSheetName() {
        if (masterSheetProperties != null) {
            return masterSheetProperties.getTitle() + " " + "V" + MyConstants.currentVersion;
        }
        return "";
    }

}