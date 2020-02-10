import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AirStation {


    public static List parameter(String param,String city) {

        Gson gson = new Gson();
        StringBuffer response = new StringBuffer();

        String url = "https://api.openaq.org/v1/latest?city=" + city;
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response: " + responseCode);

            String inputLine;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

            WCP wcp = gson.fromJson(String.valueOf(response), WCP.class);

            double sum = 0;
            int q = 0;
            double max = 0;
            double min = 0;
            double avg=0;
            double y=0;
            String date="brak danych";
            double std =0;

            double[] value = new double[wcp.results.size()];

            for (int i = 0; i < wcp.results.size(); i++) {
                for (int ii = 0; ii < wcp.results.get(i).measurements.size(); ii++) {

                    if (wcp.results.get(i).measurements.get(ii).parameter.equals(param)) {
                        date= wcp.results.get(i).measurements.get(0).lastUpdated;
                        value[q] = wcp.results.get(i).measurements.get(ii).value;


                        sum += value[q];

                        if (min == 0) {
                            min = value[q];
                        } else if (min > value[q]) {
                            min = value[q];
                        }

                        if (max < value[q]) {
                            max = value[q];
                        }
                        q++;

                    }

                }
            }

            if (q>0) {
                avg = sum / q;
                y = 0;
                for (int j = 0; j < q; j++) {
                    double x = Math.pow(value[j] - avg, 2);
                    y += x;
                }
            }
            else{
                avg=0;
            }

            if (!date.equals("brak danych")){
            date = date.replace('T', ' ');
            date = date.substring(0, 19);
                std=Math.sqrt(y / q);
        }



            //                                                      TWORZENIE LISTY WARTOSCI PARAMETRU
            ArrayList parameter = new ArrayList();
            parameter.add(param);
            parameter.add(date);
            parameter.add(index(param, avg));
            parameter.add(q);
            parameter.add(avg);
            parameter.add(std);
            parameter.add("[ug/m^3]");
            parameter.add(min);
            parameter.add(max);


//            for (int jj = 0; jj < parameter.size(); jj++) {
//                System.out.println(parameter.get(jj));
//            }
            return parameter;

    }


    public static String index(String param,Double avg){


        double[] pm10index={201,141,101,61,21,0};
        double[] pm25index={121,85,61,37,13,0};
        double[] O3index={241,181,151,121,71,0};
        double[] NO2index={401,201,151,101,41,0};
        double[] SO2index={501,351,201,101,51,0};
        String[] indexName={"Bardzo zly","Zly","Dostateczny","Umiarkowany","Dobry","Bardzo dobry","Brak danych"};
        String index=null;

        switch (param){

            case "pm10":
                for(int i=0;i<pm10index.length;i++){
                    if(avg>pm10index[i]){
                        index=indexName[i];
                        break;
                    }
                    else{
                        index=indexName[6];
                    }
                }
                break;
            case "pm25":
                for(int i=0;i<pm10index.length;i++){
                    if(avg>pm25index[i]){
                        index=indexName[i];
                        break;
                    }
                    else{
                        index=indexName[6];
                    }
                }
                break;
            case "o3":
                for(int i=0;i<pm10index.length;i++){
                    if(avg>O3index[i]){
                        index=indexName[i];
                        break;
                    }
                    else{
                        index=indexName[6];
                    }
                }
                break;
            case "no2":
                for(int i=0;i<pm10index.length;i++){
                    if(avg>NO2index[i]){
                        index=indexName[i];
                        break;
                    }
                    else{
                        index=indexName[6];
                    }
                }
                break;
            case "so2":
                for(int i=0;i<pm10index.length;i++){
                    if(avg>SO2index[i]){
                        index=indexName[i];
                        break;
                    }
                    else{
                        index=indexName[6];
                    }
                }
                break;
        }

        return index;
    }


}
