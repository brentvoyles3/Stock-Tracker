package cs1302.api;

import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import java.time.LocalTime;
import java.util.Random;
import javafx.util.Duration;
import javafx.animation.Timeline;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLEncoder;
import java.net.URL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Orientation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.KeyFrame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToolBar;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class StockLoader extends VBox {
    HBox searchPane;
    Button goButton;
    Label search;
    TextField text;
    ImageView imgview;
    Image image1, image2, image3;
    BorderPane stockPane;
    StockMenuBar menubar;
    TableView stockTable;
    TableColumn stock;
    TableColumn exchange;
    TableColumn price;
    TableColumn high;
    TableColumn low;
    TableColumn volume;
    TableColumn change;
    String Ticker;

    public StockLoader () {
        super();
        this.menubar = new StockMenuBar();
        this.searchPane = new HBox(6);
        this.goButton = new Button("Search Stocks");
        this.search = new Label("Enter Up To 3 Stock Tickers To Track: ");
        this.text = new TextField("AAPL,AMZN,MSFT");
        HBox.setHgrow(text, Priority.ALWAYS);
        searchPane.getChildren().addAll(search, text, goButton);
        searchPane.setSpacing(6);
        searchPane.setPadding(new Insets(3));
        this.setSpacing(6);
        searchPane.setAlignment(Pos.CENTER);
        goButton.setOnAction(this::loadStocks);
        stockTable = new TableView();
        stockTable.setEditable(true);
        this.stock = new TableColumn("Stock");
        this.exchange = new TableColumn("Exchange");
        this.price = new TableColumn("Price");
        this.high = new TableColumn("High");
        this.low = new TableColumn("Low");
        this.volume = new TableColumn("Volume");
        this.change = new TableColumn("Change %");
        stock.setPrefWidth(125.0d);
        price.setPrefWidth(75.0d);
        high.setPrefWidth(75.0d);
        low.setPrefWidth(75.0d);
        stockTable.getColumns().addAll(stock, exchange, price, high, low, volume, change);
        this.getChildren().addAll(menubar, searchPane, stockTable);
    } // StockLoader

    public void loadStocks (ActionEvent a) {
        String endpoint = "https://api.stockdata.org/v1/data/quote?symbols=";
        String ticker = text.getText();
        ticker = URLEncoder.encode(ticker, UTF8);
        this.Ticker = ticker;
        String apiKey = "&api_token=z8nKE4c21oTAbrqsg3NG5QeBHbRRHu8KUURhEp2I";
        String sUrl = endpoint;
        sUrl += ticker + apiKey;
        System.out.println(sUrl);
        try {
        URL url = new URL(sUrl);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonElement je = JsonParser.parseReader(reader);
        JsonObject root = je.getAsJsonObject();
        JsonArray data = root.getAsJsonArray("data");
        int numFound = data.size();
        System.out.printf("Size = %d\n", numFound);
        int index = 0;
        ObservableList<Stock> tData = FXCollections.observableArrayList();
        while (index < numFound) {
        JsonElement arrayElements = get(data, index);
        String stockName = get(arrayElements, "name").getAsString();
        System.out.println("Stock: " + stockName);
        String exchangeName = get(arrayElements, "exchange_short").getAsString();
        System.out.println("Exchange: " + exchangeName);
        String priceVal = get(arrayElements, "price").getAsString();
        System.out.println("Price: $" + priceVal);
        String highVal = get(arrayElements, "day_high").getAsString();
        System.out.println("High of Day: $" + highVal);
        String lowVal = get(arrayElements, "day_low").getAsString();
        System.out.println("Low of Day: $" + lowVal);
        String volumeVal = get(arrayElements, "volume").getAsString();
        System.out.println("Volume: " + volumeVal);
        String changeVal = get(arrayElements, "day_change").getAsString();
        System.out.println("Change: " + changeVal);
        tData.add(
            new Stock(stockName, exchangeName, priceVal, highVal, lowVal, volumeVal, changeVal));
        stock.setCellValueFactory(new PropertyValueFactory<>("name"));
        exchange.setCellValueFactory(new PropertyValueFactory<>("exchange"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        high.setCellValueFactory(new PropertyValueFactory<>("high"));
        low.setCellValueFactory(new PropertyValueFactory<>("low"));
        volume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        change.setCellValueFactory(new PropertyValueFactory<>("change"));
        stockTable.setItems(tData);
        index++;
        } // whle
//        System.out.println(tData.get(0));
//        System.out.println(tData.get(1));
//        System.out.println(tData.get(2));
        } catch (UnsupportedEncodingException uee) {
            System.err.print(uee);
            System.err.print(" Unsupported Encoding");
        } catch (MalformedURLException mue) {
            System.err.print(mue);
            System.err.print("Faulty URL");
        } catch (IOException ioe) {
            System.err.print(ioe);
            System.err.print(" Error with I/O");
        } catch (IndexOutOfBoundsException ioobe) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setResizable(true);
            alert.setHeaderText(null);
            alert.setTitle("Unavailable Stock Data");
            alert.setContentText("Invalid input. Please enter an appropriate stock ticker(AAPL)");
            alert.showAndWait();
        } // try
    } // loadStocks

        public String getTicker() {
            return this.Ticker;
        }

} // Stock Loader
