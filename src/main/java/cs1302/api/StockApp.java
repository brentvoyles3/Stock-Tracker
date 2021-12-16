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
 * Represents a stock-tracking application.
 * @author Brent Voyles
 */
public class StockApp extends Application {

    /**
     * The main method that sets the scene for a {@code StockApp}.
     * @param stage The main stage for the javafx application.
     */
    public void start(Stage stage) {
        StockLoader stockloader = new StockLoader();
        Scene scene = new Scene(stockloader);
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setTitle("Real-Time Stock Tracker");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

} // StockApp
