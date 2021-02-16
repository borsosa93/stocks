package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.*;

import java.io.IOException;


public class Main {

    static int count=0;
    static ArrayList<StockData> stocksData=new ArrayList<>();
    static ArrayList<String> symbols;
    static String filePath="C:\\Users\\t_borsa\\Documents\\Automation\\Stocks writing to CSV\\Resources\\data.xlsx";

    public static void main(String[] args) throws IOException {

        symbols=enterSymbols();
        preConnection(symbols);
        dataToExcel(stocksData);

    }

    public static ArrayList<String> enterSymbols(){

        String symbol;
        int i=0;
        ArrayList<String> constructSymbols=new ArrayList<>();
        constructSymbols.add("AAPL");
        constructSymbols.add("MSFT");
        constructSymbols.add("AMZN");
        constructSymbols.add("GOOG");
        constructSymbols.add("GOOGL");
        constructSymbols.add("FB");
        constructSymbols.add("TSLA");
        constructSymbols.add("BABA");
        constructSymbols.add("TSM");
        constructSymbols.add("V");
        constructSymbols.add("JNJ");
        constructSymbols.add("JPM");
        constructSymbols.add("WMT");
        constructSymbols.add("NVDA");
        constructSymbols.add("DIS");
        constructSymbols.add("MA");
        constructSymbols.add("PYPL");
        constructSymbols.add("UNH");
        constructSymbols.add("ALTR");
        constructSymbols.add("LPRO");
        constructSymbols.add("JCOM");
        constructSymbols.add("ACIW");
        constructSymbols.add("CR");
        constructSymbols.add("SAGE");
        constructSymbols.add("FLO");
        constructSymbols.add("CRNC");
        constructSymbols.add("FTDR");
        constructSymbols.add("UGP");
        constructSymbols.add("DOYU");
        constructSymbols.add("MSM");
        constructSymbols.add("ARNA");
        constructSymbols.add("M");
        constructSymbols.add("VLY");
        constructSymbols.add("GOCO");
        constructSymbols.add("EVR");
        constructSymbols.add("STAG");
        constructSymbols.add("AUY");
        constructSymbols.add("ACHC");
        constructSymbols.add("JOBS");
        constructSymbols.add("CLH");
        constructSymbols.add("WEN");
        constructSymbols.add("ENSG");
        constructSymbols.add("RAMP");
        constructSymbols.add("CFX");
        constructSymbols.add("CW");
        constructSymbols.add("RYN");
        constructSymbols.add("TRUP");
        constructSymbols.add("LOPE");
        constructSymbols.add("VMI");
        constructSymbols.add("HASI");
        constructSymbols.add("SRC");
        constructSymbols.add("GTES");
        constructSymbols.add("RLI");
        constructSymbols.add("ALLO");
        constructSymbols.add("PRSP");
        constructSymbols.add("RXT");
        constructSymbols.add("XRX");
        constructSymbols.add("FTI");
        constructSymbols.add("ROLL");
        constructSymbols.add("AY");
        constructSymbols.add("RPD");
        constructSymbols.add("WKHS");
        constructSymbols.add("NEOG");
        constructSymbols.add("EQT");
        constructSymbols.add("TENB");
        constructSymbols.add("NOMD");
        constructSymbols.add("SWCH");
        constructSymbols.add("LPX");
        constructSymbols.add("CBPO");
        constructSymbols.add("LPSN");
        constructSymbols.add("YALA");
        constructSymbols.add("QLYS");
        constructSymbols.add("SNX");
        constructSymbols.add("UBSI");
        constructSymbols.add("HLI");
        constructSymbols.add("AMKR");
        constructSymbols.add("JAMF");
        constructSymbols.add("DAVA");
        constructSymbols.add("HLNE");
        constructSymbols.add("WBS");
        constructSymbols.add("TREE");
        constructSymbols.add("PD");
        constructSymbols.add("NTLA");
        constructSymbols.add("WDFC");
        constructSymbols.add("GPK");
        constructSymbols.add("GOOS");
        constructSymbols.add("SLG");
        constructSymbols.add("MTSI");
        constructSymbols.add("KBR");
        constructSymbols.add("SHLX");
        constructSymbols.add("REGI");
        constructSymbols.add("NCR");

        return constructSymbols;
    }

    public static void connection(String link) {

        String line;
        StringBuffer responseContent = new StringBuffer();
        HttpURLConnection conn = null;

        try {

            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();

            //Request setup
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            //System.out.println(status);
            BufferedReader reader;
            if (status !=200) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
                parsing(responseContent.toString());
            }
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        count++;

    }

    public static void preConnection(ArrayList<String> symbols) {

        String link;
        int length=symbols.size();
        if (length==0) return;

        for(int i=0;i<length;i++) {
            link= "https://query2.finance.yahoo.com/v7/finance/quote?symbols=" + symbols.get(i);
            connection(link);
        }

    }

    public static void dataToExcel(ArrayList<StockData> stocksData) throws IOException {

        Workbook data = new XSSFWorkbook();
        Sheet sheet = data.createSheet();

            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Szimbólum");
            cell = row.createCell(1);
            cell.setCellValue("Név");
            cell = row.createCell(2);
            cell.setCellValue("Pillanatnyi érték");
            cell = row.createCell(3);
            cell.setCellValue("Mai változás:");
            cell = row.createCell(4);
            cell.setCellValue("Előző napi kereskedési volumen");
            cell = row.createCell(5);
            cell.setCellValue("Tíznapos átlagos kereskedési volumen");
            cell = row.createCell(6);
            cell.setCellValue("Ötvennapos átlagárfolyam");


        for(int i=0;i< stocksData.size();i++){
            row = sheet.createRow(i+1);

            cell = row.createCell(0);
            cell.setCellValue(stocksData.get(i).symbol);
            cell = row.createCell(1);
            cell.setCellValue(stocksData.get(i).name);
            cell = row.createCell(2);
            cell.setCellValue(stocksData.get(i).priceNow);
            cell = row.createCell(3);
            cell.setCellValue(stocksData.get(i).changePercent);
            cell = row.createCell(4);
            cell.setCellValue(stocksData.get(i).volumePreviousDay);
            cell = row.createCell(5);
            cell.setCellValue(stocksData.get(i).volumeTenDays);
            cell = row.createCell(6);
            cell.setCellValue(stocksData.get(i).fiftyDayAvg);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            data.write(outputStream);
        }

    }

    public static class StockData {

        String symbol;
        String name;
        double priceNow=-1.00;
        double changePercent=0.00;
        double fiftyDayAvg=-1.00;
        double volumeTenDays=-1.00;
        double volumePreviousDay=-1.00;

        public void setSymbol(String symbol){ this.symbol=symbol; }
        public void setName(String name){ this.name=name; }
        public void setPriceNow(double priceNow) { this.priceNow=priceNow; }
        public void setChangePercent(double changePercent) { this.changePercent=changePercent;}
        public void setFiftyDayAvg (double fiftyDayAvg) { this.fiftyDayAvg=fiftyDayAvg;}
        public void setVolumeTenDays(double volumeTenDays) { this.volumeTenDays=volumeTenDays;}
        public void setVolumePreviousDay(double volumePreviousDay) { this.volumePreviousDay=volumePreviousDay;}

    }


    public static String parsing(String responseBody){

        String name="";
        double priceNow=-1.00;
        double changePercent=0.00;
        double fiftyDayAvg=-1.00;
        double volumeTenDays=-1.00;
        double volumePreviousDay=-1.00;

        JSONObject obj=new JSONObject(responseBody);
        JSONObject quoteResponse=obj.getJSONObject("quoteResponse");
        JSONArray result=quoteResponse.getJSONArray("result");

        for(int i=0; i<result.length(); i++)
        {
            JSONObject dataField= result.getJSONObject(i);
            if((dataField.has("regularMarketPrice")))
            {
                name= dataField.getString("shortName");
                priceNow=dataField.getDouble("regularMarketPrice");
                changePercent=dataField.getDouble("regularMarketChangePercent");
                fiftyDayAvg=dataField.getDouble("fiftyDayAverage");
                volumeTenDays=dataField.getDouble("averageDailyVolume10Day");
                volumePreviousDay=dataField.getDouble("regularMarketVolume");
            }
        }

        StockData currentStock=new StockData();
        currentStock.setSymbol(symbols.get(count));
        currentStock.setName(name);
        currentStock.setPriceNow(priceNow);
        currentStock.setChangePercent(changePercent);
        currentStock.setFiftyDayAvg(fiftyDayAvg);
        currentStock.setVolumeTenDays(volumeTenDays);
        currentStock.setVolumePreviousDay(volumePreviousDay);

        stocksData.add(currentStock);

        return null;
    }

}
