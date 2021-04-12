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
    private static String SPREADSHEET_ID = MyConstants.SHEET_ID_1;

    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }

    public static void readFromSpreadSheet(int project, int start,int end) throws IOException, GeneralSecurityException {

        String sheetName = MyConstants.getSheetNameFromProject(project);

        if (sheetsService == null){
            setup();
        }

        List<String> ranges = new ArrayList<>();

        for (String key : MyConstants.colLangMapApnaLudo.keySet()){
            String str = sheetName+"!"+ key + start + ":" + key + end;
            ranges.add(str);
        }

        String valueRenderingOptions = "FORMATTED_VALUE";

        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .setValueRenderOption(valueRenderingOptions)
                .setMajorDimension("COLUMNS")
                .execute();

        List<ValueRange> valueRanges = readResult.getValueRanges();
        for (ValueRange valueRange : valueRanges){
            List<Object> objectList = valueRange.getValues().get(0);
            String lang = getLanguage(valueRange.getRange());
            if (lang == null)
                continue;

            try (XMLService_1 xmlService = new XMLService_1(lang)) {

                for (Object object : objectList) {
                    xmlService.appendFile(object.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getLanguage(String str){

        if (str == null)
            return null;

        String key = Character.toString(str.charAt(str.lastIndexOf("!")+1));
        return MyConstants.colLangMapApnaLudo.get(key);
    }
}