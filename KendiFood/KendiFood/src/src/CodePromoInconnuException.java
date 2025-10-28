package src;


public class CodePromoInconnuException extends KendiFoodException {
    public CodePromoInconnuException(String code) {
        super("Code promo inconnu : \"" + code + "\"");
    }
}