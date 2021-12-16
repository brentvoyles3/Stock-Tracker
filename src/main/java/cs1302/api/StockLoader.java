package cs1302.api;

import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import java.time.LocalTime;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



/**
 * Represents the stock loading process. Implements multiple API's
 * by pulling responses and returning specified stock data.
 *
 */
public class StockLoader extends VBox {
    HBox searchPane;
    Button goButton, stock1, stock2, stock3;
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
    BorderPane mainPane;
    TextField newsString;
    TextArea textPane;
    String stockkey;
    String newskey;
    String configPath = "resources/config.properties";

    /**
     * Loads and sets the two API keys.
     *
     */
    public void setKeys() {
        try (FileInputStream configFileStream = new FileInputStream(configPath)) {
            Properties config = new Properties();
            config.load(configFileStream);
            stockkey = config.getProperty("stockdata.apikey");
            newskey = config.getProperty("newsapi.apikey");
        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        } // try
    }

    /**
     * Default constructor for a StockLoader.
     */
    public StockLoader () {
        super();
        BorderPane mainPane = new BorderPane();
        setSearchPane();
        this.setSpacing(6);
        StackPane searchpane = new StackPane(searchPane);
        mainPane.setTop(searchpane);
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
        StackPane tablepane = new StackPane(stockTable);
        tablepane.setPrefHeight(105);
        mainPane.setCenter(tablepane);
        VBox textspace = new VBox();
        HBox buttonContainer = new HBox();
        setButtons();
        Label inquireNews = new Label("Top Headlines");
        buttonContainer.getChildren().addAll(inquireNews, stock1, stock2, stock3);
        buttonContainer.setSpacing(5);
        buttonContainer.setAlignment(Pos.CENTER);
        textPane = new TextArea();
        newsString = new TextField();
        Label news = new Label("Stock Market News");
        textspace.getChildren().addAll(buttonContainer, textPane, news);
        StackPane space = new StackPane(textspace);
        space.setPrefHeight(210);
        mainPane.setBottom(space);
        this.getChildren().addAll(menubar, mainPane);
        stock1.setOnAction(this::setNewsA);
        stock2.setOnAction(this::setNewsB);
        stock3.setOnAction(this::setNewsC);
        setKeys();
    } // StockLoader

    /**
     * Setter for the buttons on the news pane.
     */
    public void setButtons () {
        stock1 = new Button("Stock A");
        stock1.setDisable(true);
        stock2 = new Button("Stock B");
        stock2.setDisable(true);
        stock3 = new Button("Stock C");
        stock3.setDisable(true);
    }

    /**
     * Creates and instantiates a search bar in order to input stocks.
     */
    public void setSearchPane () {
        this.menubar = new StockMenuBar();
        this.searchPane = new HBox(6);
        this.goButton = new Button("Search Stocks");
        this.search = new Label("Enter Up To 3 Stock Tickers To Track: ");
        this.text = new TextField("AAPL,CRWD,MSFT");
        searchPane.getChildren().addAll(search, text, goButton);
        searchPane.setSpacing(6);
        searchPane.setPadding(new Insets(3));
        searchPane.setAlignment(Pos.CENTER);
    }

/**
 * Loads stock data by requesting from the StockData API.
 * @param a Represents the action of pulling data from the StockData API
 * and storing the data.
 */
    //First API Response - StockData (Grab Stock Response)
    public void loadStocks (ActionEvent a) {
        resetButtons();
        resetTextArea();
        String endpoint = "https://api.stockdata.org/v1/data/quote?symbols=";
        String ticker = text.getText();
        ticker = URLEncoder.encode(ticker, UTF8);
        String apiKey = "&api_token=";
        String sUrl = endpoint;
        sUrl += ticker + apiKey + stockkey;
        try {
            URL url = new URL(sUrl);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject();
            JsonArray data = root.getAsJsonArray("data");
            int numFound = data.size();
            int index = 0;
            ObservableList<Stock> tData = FXCollections.observableArrayList();
            while (index < numFound) {
                JsonElement arrayElements = get(data, index);
                String stockName = get(arrayElements, "name").getAsString();
                String exchangeName = get(arrayElements, "exchange_short").getAsString();
                String priceVal = get(arrayElements, "price").getAsString();
                String highVal = get(arrayElements, "day_high").getAsString();
                String lowVal = get(arrayElements, "day_low").getAsString();
                String volumeVal = get(arrayElements, "volume").getAsString();
                String changeVal = get(arrayElements, "day_change").getAsString();
                String symbol = get(arrayElements, "ticker").getAsString();
                setSymbols(index, symbol);
                tData.add(
                    new Stock(stockName, exchangeName, priceVal,
                              highVal, lowVal, volumeVal, changeVal));
                stock.setCellValueFactory(new PropertyValueFactory<>("name"));
                exchange.setCellValueFactory(new PropertyValueFactory<>("exchange"));
                price.setCellValueFactory(new PropertyValueFactory<>("price"));
                high.setCellValueFactory(new PropertyValueFactory<>("high"));
                low.setCellValueFactory(new PropertyValueFactory<>("low"));
                volume.setCellValueFactory(new PropertyValueFactory<>("volume"));
                change.setCellValueFactory(new PropertyValueFactory<>("change"));
                stockTable.setItems(tData);
                index++;
            } // while
            checkButtons();
        } catch (UnsupportedEncodingException uee) {
            System.err.print(uee);
        } catch (MalformedURLException mue) {
            System.err.print(mue);
        } catch (IOException ioe) {
            System.err.print(ioe);
        } catch (IndexOutOfBoundsException ioobe) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setResizable(true);
            alert.setHeaderText(null);
            alert.setTitle("Unavailable Stock Data");
            alert.setContentText("Invalid input. Please enter appropriate stock tickers");
            alert.showAndWait();
        } // try
    } // loadStocks

    /**
     * Sets the button symbol once the data has been received.
     * @param index The index of the API data array.
     * @param symbol The symbol represented on the button.
     */
    public void setSymbols(int index, String symbol) {
        if (index == 0) {
            stock1.setText("$" + symbol);
        }
        if (index == 1) {
            stock2.setText("$" + symbol);
        }
        if (index == 2) {
            stock3.setText("$" + symbol);
        }
    }

    /**
     * Checks if each button needs to be disabled. If the
     * button needs to be disabled, then it is disabled. Otherwise,
     * does nothing.
     */
    public void checkButtons() {
        if (stock1.getText().equals("Stock A")) {
            stock1.setDisable(true);
        }
        if (stock2.getText().equals("Stock B")) {
            stock2.setDisable(true);
        }
        if (stock3.getText().equals("Stock C")) {
            stock3.setDisable(true);
        }
    } // checkButtons

    /**
     * Resets the buttons to original state.
     */
    public void resetButtons() {
        stock1.setDisable(false);
        stock1.setText("Stock A");
        stock1.setStyle(null);
        stock2.setDisable(false);
        stock2.setText("Stock B");
        stock2.setStyle(null);
        stock3.setDisable(false);
        stock3.setText("Stock C");
        stock3.setStyle(null);
    }

    /**
     * Sets the text area to display news regarding the first stock entry. Pulls
     * responses from the news api.
     * @param b Action to load and set the news regarding the first stock entry.
     */
//Second API Response - NewsApi (Pull Stock News Response)
    public void setNewsA(ActionEvent b) {
        resetTextArea();
        stock2.setStyle(null);
        stock3.setStyle(null);
        stock1.setStyle("-fx-background-color: #008000; ");
        String stocka = "(" + stock1.getText() + ")";
        stocka = URLEncoder.encode(stocka, UTF8);
        String endpoint = "https://newsapi.org/v2/everything?qInTitle=" + stocka +
            "&sortedBy=publishedAt&pageSize=8";
        //String apiKey = "&apiKey=da0a6e229ea740b1901b31c158ec752c";
        String apiKey = "&apiKey=";
        String sUrl = endpoint + apiKey + newskey;
        try {
            URL url = new URL(sUrl);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject();
            JsonArray articles = root.getAsJsonArray("articles");
            int numFound = articles.size();
            int index = 0;
            while (index < numFound) {
                JsonElement articlesElement = get(articles, index);
                String title = get(articlesElement, "title").getAsString();
                String date = get(articlesElement, "publishedAt").getAsString();
                date = date.substring(0,date.indexOf("T"));
                textPane.appendText(date + " " + title  + "\n");
                index++;
            }
        } catch (UnsupportedEncodingException uee) {
            System.err.print(uee);
            uee.printStackTrace();
        } catch (MalformedURLException mue) {
            System.err.print(mue);
            mue.printStackTrace();
        } catch (IOException ioe) {
            System.err.print(ioe);
            ioe.printStackTrace();
        }
    } // loadNewsA

    /**
     * Sets the text area to display news for the second stock entry. Pulls
     * responses from the news api.
     * @param c Action to set the text to news regarding the second stock entry.
     */
    public void setNewsB(ActionEvent c) {
        resetTextArea();
        stock1.setStyle(null);
        stock3.setStyle(null);
        stock2.setStyle("-fx-background-color: #008000; ");
        String stockb = stock2.getText();
        stockb = URLEncoder.encode(stockb, UTF8);
        String endpoint = "https://newsapi.org/v2/everything?qInTitle=" + stockb +
            "&sortBy=relevancy&pageSize=8";
        String apiKey = ("&apiKey=");
        String sUrl = endpoint + apiKey + newskey;
        try {
            URL url = new URL(sUrl);
            InputStreamReader read = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(read);
            JsonObject root = je.getAsJsonObject();
            JsonArray articles = root.getAsJsonArray("articles");
            int numFound = articles.size();
            int index = 0;
            while (index < numFound) {
                JsonElement articlesElement = get(articles, index);
                String title = get(articlesElement, "title").getAsString();
                String date = get(articlesElement, "publishedAt").getAsString();
                date = date.substring(0,date.indexOf("T"));
                textPane.appendText(date + " " + title + "\n");
                index++;
            } // while
        } catch (UnsupportedOperationException uoe) {
            System.err.print(uoe);
            uoe.printStackTrace();
        } catch (MalformedURLException mue) {
            System.err.print(mue);
            mue.printStackTrace();
        } catch (IOException ioe) {
            System.err.print(ioe);
            ioe.printStackTrace();
        } // try
    } // loadNewsB

    /**
     * Sets the text area to display news for the third stock entry. Pulls responses from
     * the news api.
     * @param d Action to set the text area to resemble news for the third stock entry.
     */
    public void setNewsC(ActionEvent d) {
        resetTextArea();
        stock1.setStyle(null);
        stock2.setStyle(null);
        stock3.setStyle("-fx-background-color: #008000; ");
        String stockc = stock3.getText();
        stockc = URLEncoder.encode(stockc, UTF8);
        String endpoint = "https://newsapi.org/v2/everything?qInTitle=" + stockc +
            "&sortBy=relevancy&pageSize=8";
        String apiKey = "&apiKey=";
        String sUrl = endpoint + apiKey + newskey;
        try {
            URL url = new URL(sUrl);
            InputStreamReader read = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(read);
            JsonObject root = je.getAsJsonObject();
            JsonArray articles = root.getAsJsonArray("articles");
            int numFound = articles.size();
            int index = 0;
            while (index < numFound) {
                JsonElement articlesElement = get(articles, index);
                String title = get(articlesElement, "title").getAsString();
                String date = get(articlesElement, "publishedAt").getAsString();
                date = date.substring(0,date.indexOf("T"));
                textPane.appendText(date + " " + title + "\n");
                index++;
            } // while
        } catch (UnsupportedEncodingException uee) {
            System.err.print(uee);
            uee.printStackTrace();
        } catch (MalformedURLException mue) {
            System.err.print(mue);
            mue.printStackTrace();
        } catch (IOException ioe) {
            System.err.print(ioe);
            ioe.printStackTrace();
        } // try
    } // setNewsC

    /**
     * Resets the text area.
     */
    public void resetTextArea() {
        textPane.clear();
    } // resetTextArea

} // StockLoader
