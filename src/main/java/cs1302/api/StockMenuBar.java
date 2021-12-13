package cs1302.api;

import javafx.scene.layout.HBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Priority;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class StockMenuBar extends HBox {
    MenuBar menu;
    Menu file;
    MenuItem exit;
    Menu help;
    MenuItem viewHelp;

    public StockMenuBar () {
    super();
    this.menu = new MenuBar();
    this.file = new Menu("File");
    this.help = new Menu("Help");
    this.viewHelp = new MenuItem("View Help");
    viewHelp.setOnAction((ActionEvent a) -> {
            Alert howTo = new Alert(AlertType.INFORMATION);
            howTo.setResizable(true);
            howTo.setHeaderText(null);
            howTo.setTitle("How to use");
            howTo.setContentText("Example:AAPL, TSLA, AMZN, GOOG");
            howTo.showAndWait();
        });
    help.getItems().add(viewHelp);
    this.exit = new MenuItem("Exit");
    exit.setOnAction((ActionEvent e) -> System.exit(0));
    file.getItems().add(exit);
    menu.getMenus().addAll(file, help);
    this.getChildren().add(menu);
    this.setHgrow(menu, Priority.ALWAYS);
    } // Default Construct
} // StockMenuBar
