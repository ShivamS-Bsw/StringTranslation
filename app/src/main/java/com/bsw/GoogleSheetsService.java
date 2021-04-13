package com.bsw;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DuplicateSheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GoogleSheetsService {
    private static Sheets sheetsService;
    private static String SPREADSHEET_ID = MyConstants.SHEET_ID_3;
    private static SheetProperties masterSheetProperties;
    private static long max = Long.MAX_VALUE;
    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
        masterSheetProperties = getSpreadSheetPropertiesData();
    }

    public static void readFromSpreadSheet(int project) throws IOException, GeneralSecurityException {

        String sheetName = MyConstants.getSheetNameFromProject(project);

        if (sheetsService == null){
            setup();
        }

        List<String> ranges = new ArrayList<>();
        ranges.add(sheetName);

        String valueRenderingOptions = "FORMATTED_VALUE";

        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .setValueRenderOption(valueRenderingOptions)
                .setMajorDimension("COLUMNS")
                .execute();

        List<ValueRange> valueRanges = readResult.getValueRanges();
        for (ValueRange valueRange : valueRanges){
            List<List<Object>> colList = valueRange.getValues();

            for (List<Object> objects : colList){

                if (objects == null || objects.size() == 0)
                    continue;

                String lang = objects.get(0).toString(); // Lang Coloumn
                max = max - objects.size();
                try (XMLService_1 xmlService_1 = new XMLService_1(lang)) {
                    for (int i = 1; i < objects.size(); i++) {
                        String data = objects.get(i).toString();

                        if (checkForValidData(data)){
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

    public static void readFromSpreadSheetForSingleLang(int project, String l) throws IOException, GeneralSecurityException {

        String sheetName = MyConstants.getSheetNameFromProject(project);

        if (sheetsService == null){
            setup();
        }

        if (l == null || l.trim().isEmpty())
            return;

        List<String> ranges = new ArrayList<>();
        ranges.add(sheetName);

        String valueRenderingOptions = "FORMATTED_VALUE";

        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .setValueRenderOption(valueRenderingOptions)
                .setMajorDimension("COLUMNS")
                .execute();

        List<ValueRange> valueRanges = readResult.getValueRanges();
        for (ValueRange valueRange : valueRanges){
            List<List<Object>> colList = valueRange.getValues();

            for (List<Object> objects : colList){

                if (objects == null || objects.size() == 0)
                    continue;
                String lang = objects.get(0).toString(); // Lang Coloumn
                max = max - objects.size();
                if (lang.equals(l)){
                    try (XMLService_1 xmlService_1 = new XMLService_1(lang)) {
                        for (int i = 1; i < objects.size(); i++) {
                            String data = objects.get(i).toString();

                            if (checkForValidData(data)){
                                xmlService_1.appendFile(data.trim());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Something has written to that file
            checkIfNewDataHasAdded();
        }
    }

    private static void checkIfNewDataHasAdded() throws GeneralSecurityException, IOException {
        if (max != Long.MAX_VALUE){
            saveLogs();
        }
    }

    private static boolean checkForValidData(String data){

        return !data.trim().isEmpty() || data.contains("<");

    }

    public static SheetProperties getSpreadSheetPropertiesData() throws IOException, GeneralSecurityException {
        if (sheetsService == null){
            setup();
        }

        Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(MyConstants.SHEET_ID_3);
        Spreadsheet response = request.execute();

        if (response == null){
            System.out.println("Failed to fetch properties");
            System.exit(0);
        }

        List<Sheet> sheetsList = response.getSheets();
        for (Sheet sheet : sheetsList){
            if (sheet.getProperties().getTitle().equals(MyConstants.LUDO_SUPERSTAR)){
                return masterSheetProperties = sheet.getProperties();
            }
        }
        return null;
    }

    public static void saveLogs() throws GeneralSecurityException, IOException {

        if (sheetsService == null){
            setup();
        }

        if (masterSheetProperties == null){
            getSpreadSheetPropertiesData();
        }

        List<Request> requests = new ArrayList<>();
        requests.add(new Request()
                .setDuplicateSheet(
                        new DuplicateSheetRequest()
                                .setNewSheetName(generateNewSheetName())
                                .setSourceSheetId(masterSheetProperties.getSheetId())
                                .setNewSheetId(getRandomSheetId())
                                .setInsertSheetIndex(masterSheetProperties.getIndex()+1)
                )
        );
        BatchUpdateSpreadsheetRequest body =
                new BatchUpdateSpreadsheetRequest()
                        .setRequests(requests)
                ;
        BatchUpdateSpreadsheetResponse response =
                sheetsService.spreadsheets().batchUpdate(MyConstants.SHEET_ID_3, body).execute();

        System.out.println("Logging Success");

        writeToVersionFile();
    }

    private static void readFromVersionFile() throws GeneralSecurityException, IOException {

        if (sheetsService == null){
            setup();
        }

        List<String> ranges = new ArrayList<>();
        ranges.add("Sheet1");

        String valueRenderingOptions = "FORMATTED_VALUE";

        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .setValueRenderOption(valueRenderingOptions)
                .setMajorDimension("COLUMNS")
                .execute();

        List<ValueRange> valueRanges = readResult.getValueRanges();
        for (ValueRange valueRange : valueRanges){
            List<List<Object>> colList = valueRange.getValues();
            List<Object> object = colList.get(3);

            if (object != null && object.size() > 0){
                MyConstants.currentVersion = (double) object.get(object.size()-1);
            }
        }
    }

    private static void writeToVersionFile() throws IOException {


        List<List<Object>> values = Arrays.asList(
                Arrays.asList(
                        getCurrentTimeStamp(),
                        MyConstants.VERSION_FILE_SHEET_ID,
                        masterSheetProperties.getTitle(),
                        "2.0"
                        )
                // Additional rows ...
        );

        ValueRange body = new ValueRange()
                .setValues(values);

        AppendValuesResponse result =
                sheetsService.spreadsheets().values().append(MyConstants.VERSION_FILE_SHEET_ID,"Sheet1", body)
                        .setValueInputOption("RAW")
                        .execute();
    }

    private static Integer getRandomSheetId(){
        return new Random().nextInt(9999);
    }

    private static String getCurrentTimeStamp(){
        return new Date().toString();
    }
//    private static double incrementVersion(){
//        return MyConstants.DEFAULT_VERSION + 1;
//    }
    private static String generateNewSheetName(){
        if (masterSheetProperties != null){
            return masterSheetProperties.getTitle()+" V" + "2.0";
        }
        return "";
    }

}