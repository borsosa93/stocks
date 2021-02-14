package com.company;

import org.json.JSONArray;
import org.json.JSONObject;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


public class Main {

    static int count=0;
    static ArrayList<StockData> stocksData=new ArrayList<>();
    static ArrayList<String> symbols;
    static String done="done"; //ezzel lehet megvaltoztatni, hogy milyen kulcsszot ker az utolso reszveny utan

    public static void main(String[] args)  {

        System.out.println("Írd be a részvény jelét, majd ENTER, hogy rákeress. Írd be, hogy \"" +done+"\", miután megadtad az utolsót");

        symbols=enterSymbols();
        preConnection(symbols);

        for(int i=0;i< stocksData.size();i++){
            System.out.println("\nNév:"+stocksData.get(i).name);
            System.out.println("Jel:"+stocksData.get(i).symbol);
            System.out.println("Pillanatnyi érték:"+stocksData.get(i).priceNow+" USD");
            System.out.println("Mai változás:"+stocksData.get(i).changePercent+" %");
            System.out.println("Ötvennapos átlagárfolyam:"+stocksData.get(i).fiftyDayAvg+" USD");
            System.out.println("Tíznapos kereskedési volumen:"+stocksData.get(i).volumeTenDays+" USD");
        }

    }

    public static ArrayList<String> enterSymbols(){

        String symbol;
        int i=0;
        Scanner in = new Scanner(System.in);
        ArrayList<String> constructSymbols=new ArrayList<>();

        while(true) {
            System.out.print("Kérem a részvény jelét nagybetűkkel:");
            symbol = in.nextLine();
            if(symbol.equals(done))break;
            constructSymbols.add(symbol);
            i++;
        }
        return constructSymbols;
    }

    public static void connection(String link){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parsing)
                .join();    //ez zarja le a sendAsync-ot
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

    public static String searchCsvLine(String searchString) throws IOException {
        String resultRow = null;
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\t_borsa\\Documents\\Automation\\NASDAQ-stocks.csv"));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(",");
            if(values[0].equals(searchString)) {
                resultRow = line;
                break;
            }
        }
        br.close();
        System.out.println(resultRow);
        return resultRow;
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
            }
        }

        StockData currentStock=new StockData();
        currentStock.setSymbol(symbols.get(count));
        currentStock.setName(name);
        currentStock.setPriceNow(priceNow);
        currentStock.setChangePercent(changePercent);
        currentStock.setFiftyDayAvg(fiftyDayAvg);
        currentStock.setVolumeTenDays(volumeTenDays);
        
        stocksData.add(currentStock);

        return null;
    }

}
