package cs1302.api;

import javafx.application.Application;

/**
 * The main entry point for a StockApp Application.
 * @param args An array for the command line arguments.
 */
public class StockDriver {

    public static void main (String[] args ) {
        try {
            Application.launch(StockApp.class, args);
        } catch (UnsupportedOperationException uoe) {
            System.out.println(uoe);
            System.err.print("Unsupported");
        } // try
    } // main
} // StockDriver
