import java.io.*;
import java.net.URL;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BarChart<?, ?> chart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private TableView<Paramater> table;

    @FXML
    private TextField city;

    @FXML
    private Button btnFind;

    @FXML
    private TextField path;

    @FXML
    private Button btnLoadPath;

    @FXML
    private Button btnSave;

    @FXML
    private TextField title;


    @FXML
    void OnClickBTNLoadPath(ActionEvent event) {
        tableCreator(getParameter2());
    }

    @FXML
    void onClickBTNSave(ActionEvent event) {
        saveToJason();
    }

    @FXML
    void onClickbtnFind(ActionEvent event) {
        tableCreator(getParameter());
    }

    private List pm10;
    private List pm25;
    private List O3;
    private List NO2;
    private List SO2;


    public void saveToJason() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("D:/fromProject/wynik.json");

        ObservableList<Paramater> parameter;
        parameter = params(pm10, pm25, O3, NO2, SO2);

        try (FileWriter fileWriter = new FileWriter(file)) {

            gson.toJson(parameter, fileWriter);

        } catch (IOException e) {
            System.out.println("IO error");
        }

    }

    public void tableCreator(ObservableList<Paramater> paramaters) {
        table.getColumns().clear();

        TableColumn<Paramater, String> nameColumn = new TableColumn<>("Parametr");
        nameColumn.setMaxWidth(80);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Paramater, String> dateColumn = new TableColumn<>("Data");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Paramater, String> indexColumn = new TableColumn<>("Indeks jakosci powietrza");
        indexColumn.setMinWidth(100);
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));

        TableColumn<Paramater, Integer> quantityColumn = new TableColumn<>("Ilosc pomiarow");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Paramater, Double> avgColumn = new TableColumn<>("Srednia");
        avgColumn.setMaxWidth(70);
        avgColumn.setCellValueFactory(new PropertyValueFactory<>("avg"));

        TableColumn<Paramater, Double> stdColumn = new TableColumn<>("Odchylenie standardowe");
        stdColumn.setMinWidth(100);
        stdColumn.setCellValueFactory(new PropertyValueFactory<>("std"));

        TableColumn<Paramater, String> unitColumn = new TableColumn<>("Jednostka");
        unitColumn.setMinWidth(70);
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));

        TableColumn<Paramater, Double> minColumn = new TableColumn<>("Minimum");
        minColumn.setMaxWidth(80);
        minColumn.setCellValueFactory(new PropertyValueFactory<>("min"));

        TableColumn<Paramater, Double> maxColumn = new TableColumn<>("Maksimum");
        maxColumn.setMaxWidth(80);
        maxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));


        table.setItems(paramaters);
        table.getColumns().addAll(nameColumn, dateColumn, indexColumn, quantityColumn, avgColumn, stdColumn, unitColumn, minColumn, maxColumn);


    }

    public void chartCreator(double[] values) {
        chart.getData().clear();
        XYChart.Series normal = new XYChart.Series<>();
        normal.getData().add(new XYChart.Data("pm 10", 141));
        normal.getData().add(new XYChart.Data("pm 2.5", 85));
        normal.getData().add(new XYChart.Data("O3", 181));
        normal.getData().add(new XYChart.Data("NO2", 201));
        normal.getData().add(new XYChart.Data("SO2", 351));

        chart.getData().addAll(normal);
        XYChart.Series value = new XYChart.Series<>();
        value.getData().add(new XYChart.Data("pm 10", values[0]));
        value.getData().add(new XYChart.Data("pm 2.5", values[1]));
        value.getData().add(new XYChart.Data("O3", values[2]));
        value.getData().add(new XYChart.Data("NO2", values[3]));
        value.getData().add(new XYChart.Data("SO2", values[4]));
        chart.getData().addAll(value);

    }


    public ObservableList<Paramater> getParameter() {            //FROM AIRSTATION
        ObservableList<Paramater> parameter;
        pm10 = AirStation.parameter("pm10", city.getText());
        pm25 = AirStation.parameter("pm25", city.getText());
        O3 = AirStation.parameter("o3", city.getText());
        NO2 = AirStation.parameter("no2", city.getText());
        SO2 = AirStation.parameter("so2", city.getText());
        parameter = params(pm10, pm25, O3, NO2, SO2);


        double[] values = new double[5];

        for (int i = 0; i < values.length; i++) {
            values[i] = parameter.get(i).getAvg();
        }

        chartCreator(values);

        return parameter;
    }

    public ObservableList<Paramater> getParameter2() {               //FROM FILE
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String filePath = path.getText();
        Paramater[] fromJson = null;
        File file = new File(filePath);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            fromJson = gson.fromJson(bufferedReader, Paramater[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Paramater pm10 = fromJson[0];
        Paramater pm25 = fromJson[1];
        Paramater O3 = fromJson[2];
        Paramater NO2 = fromJson[3];
        Paramater SO2 = fromJson[4];
        ObservableList<Paramater> parameter = FXCollections.observableArrayList();
        parameter.add(pm10);
        parameter.add(pm25);
        parameter.add(O3);
        parameter.add(NO2);
        parameter.add(SO2);

        double[] values = new double[5];

        for (int i = 0; i < values.length; i++) {
            values[i] = parameter.get(i).getAvg();
        }

        chartCreator(values);
        return parameter;
    }

    public ObservableList<Paramater> params(List pm10, List pm25, List O3, List NO2, List SO2) {
        ObservableList<Paramater> parameter = FXCollections.observableArrayList();
        parameter.add(new Paramater((String) pm10.get(0), (String) pm10.get(1),
                (String) pm10.get(2), (int) pm10.get(3), (double) pm10.get(4), (double) pm10.get(5), (String) pm10.get(6), (double) pm10.get(7), (double) pm10.get(8)));

        parameter.add(new Paramater((String) pm25.get(0), (String) pm25.get(1),
                (String) pm25.get(2), (int) pm25.get(3), (double) pm25.get(4), (double) pm25.get(5), (String) pm25.get(6), (double) pm25.get(7), (double) pm25.get(8)));

        parameter.add(new Paramater((String) O3.get(0), (String) O3.get(1),
                (String) O3.get(2), (int) O3.get(3), (double) O3.get(4), (double) O3.get(5), (String) O3.get(6), (double) O3.get(7), (double) O3.get(8)));

        parameter.add(new Paramater((String) NO2.get(0), (String) NO2.get(1),
                (String) NO2.get(2), (int) NO2.get(3), (double) NO2.get(4), (double) NO2.get(5), (String) NO2.get(6), (double) NO2.get(7), (double) NO2.get(8)));

        parameter.add(new Paramater((String) SO2.get(0), (String) SO2.get(1),
                (String) SO2.get(2), (int) SO2.get(3), (double) SO2.get(4), (double) SO2.get(5), (String) SO2.get(6), (double) SO2.get(7), (double) SO2.get(8)));
        return parameter;

    }


    @FXML
    void initialize() {

        assert chart != null : "fx:id=\"chart\" was not injected: check your FXML file 'graph.fxml'.";
        assert categoryAxis != null : "fx:id=\"categoryAxis\" was not injected: check your FXML file 'graph.fxml'.";
        assert numberAxis != null : "fx:id=\"numberAxis\" was not injected: check your FXML file 'graph.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'graph.fxml'.";
        assert city != null : "fx:id=\"city\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnFind != null : "fx:id=\"btnFind\" was not injected: check your FXML file 'graph.fxml'.";
        assert path != null : "fx:id=\"path\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnLoadPath != null : "fx:id=\"btnLoadPath\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'graph.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'graph.fxml'.";

        chart.getData().clear();
        XYChart.Series normal = new XYChart.Series<>();
        normal.getData().add(new XYChart.Data("pm 10", 141));
        normal.getData().add(new XYChart.Data("pm 2.5", 85));
        normal.getData().add(new XYChart.Data("O3", 181));
        normal.getData().add(new XYChart.Data("NO2", 201));
        normal.getData().add(new XYChart.Data("SO2", 351));

        chart.getData().addAll(normal);

    }
}

