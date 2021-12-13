package cs1302.api;

import javafx.beans.property.SimpleStringProperty;

public class Stock{
    private final SimpleStringProperty name, exchange, price, high, low, volume, change;

    public Stock (String sName, String sExchange, String sPrice, String sHigh, String sLow ,String sVolume, String sChange) {
        this.name = new SimpleStringProperty(sName);
        this.exchange = new SimpleStringProperty(sExchange);
        this.price = new SimpleStringProperty(sPrice);
        this.high = new SimpleStringProperty(sHigh);
        this.low = new SimpleStringProperty(sLow);
        this.volume = new SimpleStringProperty(sVolume);
        this.change = new SimpleStringProperty(sChange);
    }

    public String getName() {
        return name.get();
    }
    public String getExchange() {
        return exchange.get();
    }
    public String getPrice() {
        return price.get();
    }
    public String getHigh() {
        return high.get();
    }
    public String getLow() {
        return low.get();
    }
    public String getVolume() {
        return volume.get();
    }
    public String getChange() {
        return change.get();
    }
}
