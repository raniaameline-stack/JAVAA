package src;

public class CodePromo {
    private String code;
    private int pourcentage;
    
    public CodePromo(String code, int pourcentage) throws DonneesInvalidesException {
        setCode(code);
        setPourcentage(pourcentage);
    }
    
    public String getCode() { return code; }
    public void setCode(String code) throws DonneesInvalidesException {
        if (code == null || code.trim().isEmpty()) {
            throw new DonneesInvalidesException("Le code ne peut pas être vide");
        }
        this.code = code.trim();
    }
    
    public int getPourcentage() { return pourcentage; }
    public void setPourcentage(int pourcentage) throws DonneesInvalidesException {
        if (pourcentage < 1 || pourcentage > 50) {
            throw new DonneesInvalidesException("Le pourcentage doit être entre 1 et 50");
        }
        this.pourcentage = pourcentage;
    }
}