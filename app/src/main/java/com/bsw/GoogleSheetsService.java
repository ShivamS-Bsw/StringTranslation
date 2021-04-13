package com.bsw;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleSheetsService {
    private static Sheets sheetsService;
    private static String SPREADSHEET_ID = MyConstants.SHEET_ID_3;

    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
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
    }

    private static boolean checkForValidData(String data){

        if (data.trim().isEmpty() && !data.contains("<"))
            return false;
        return true;

    }
}