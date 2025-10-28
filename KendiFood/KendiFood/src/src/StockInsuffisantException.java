package src;

public class StockInsuffisantException extends KendiFoodException {
    public StockInsuffisantException(String id, int stockDisponible) {
        super("Stock insuffisant pour l'article " + id + " (disponible: " + stockDisponible + ")");
    }
}