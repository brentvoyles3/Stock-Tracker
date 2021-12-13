package cs1302.api;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Represents an event-finding application!
 *
 *
 */
public class StockApp extends Application {

    public void start(Stage stage) {
        StockLoader stockloader = new StockLoader();
        StackPane loaderPane = new StackPane(stockloader);
        loaderPane.setPrefHeight(175);
        StockNews news = new StockNews();
        StackPane newsPane = new StackPane(news);
        BorderPane root = new BorderPane();
        root.setTop(loaderPane);
        root.setBottom(newsPane);
        Scene scene = new Scene(root);
        scene.setFill(Color.GREEN);
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setTitle("Real-Time Stock Tracker");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

} // StockApp
