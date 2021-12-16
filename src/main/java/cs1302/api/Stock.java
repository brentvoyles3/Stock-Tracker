package cs1302.api;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a publicly traded Stock.
 *
 */
public class Stock {
    private final SimpleStringProperty name, exchange, price, high, low, volume, change;

    /**
     * Default constructor for a Stock object.
     *
     * @param sName The name of the Stock.
     * @param sExchange The exchange where the stock is traded.
     * @param sPrice The price of the stock.
     * @param sHigh The stocks daily high price.
     * @param sLow The stocks daily low price.
     * @param sVolume The volume of the stock.
     * @param sChange The stock's change in price.
     */
    public Stock (String sName, String sExchange, String sPrice, String sHigh,
                  String sLow, String sVolume, String sChange) {
        this.name = new SimpleStringProperty(sName);
        this.exchange = new SimpleStringProperty(sExchange);
        this.price = new SimpleStringProperty(sPrice);
        this.high = new SimpleStringProperty(sHigh);
        this.low = new SimpleStringProperty(sLow);
        this.volume = new SimpleStringProperty(sVolume);
        this.change = new SimpleStringProperty(sChange);
    } // Stock

/**
 * Gets the name of the stock.
 * @return A String containing the stock's name.
 */
    public String getName() {
        return name.get();
    } // getName

/**
 * Gets the name of the exchange the stock is traded within.
 * @return A String containing the name of the exchange.
 */
    public String getExchange() {
        return exchange.get();
    } // getPrice

/**
 * Gets the price of the stock.
 * @return A String containing the price of the stock.
 */
    public String getPrice() {
        return price.get();
    } // getPrice

/**
 * Gets the stock's highest price for current day.
 * @return A String containing the daily high price of the stock.
 */
    public String getHigh() {
        return high.get();
    } // getHigh

/**
 * Gets the stock's lowest price for the current day.
 * @return A String containing the daily low price of the stock.
 */
    public String getLow() {
        return low.get();
    } // getLow

/**
 * Gets the stock's current volume for the current day.
 * @return A String containing the daily volume of the stock.
 */
    public String getVolume() {
        return volume.get();
    } // getVolume

/**
 * Gets the daily change in price of the stock.
 * @return A String containing the stock's change in price.
 */
    public String getChange() {
        return change.get();
    } // getChange

} // Stock
