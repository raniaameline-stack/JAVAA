package src;

public class Article {
    private String id;
    private String libelle;
    private int prixCentimes;
    private int stock;
    
    public Article(String id, String libelle, int prixCentimes, int stock) throws DonneesInvalidesException {
        setId(id);
        setLibelle(libelle);
        setPrixCentimes(prixCentimes);
        setStock(stock);
    }
    
    // Getters et setters avec validation
    public String getId() { return id; }
    public void setId(String id) throws DonneesInvalidesException {
        if (id == null || id.trim().isEmpty()) {
            throw new DonneesInvalidesException("L'id ne peut pas être vide");
        }
        this.id = id.trim();
    }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) throws DonneesInvalidesException {
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new DonneesInvalidesException("Le libellé ne peut pas être vide");
        }
        this.libelle = libelle.trim();
    }
    
    public int getPrixCentimes() { return prixCentimes; }
    public void setPrixCentimes(int prixCentimes) throws DonneesInvalidesException {
        if (prixCentimes < 0) {
            throw new DonneesInvalidesException("Le prix ne peut pas être négatif");
        }
        this.prixCentimes = prixCentimes;
    }
    
    public int getStock() { return stock; }
    public void setStock(int stock) throws DonneesInvalidesException {
        if (stock < 0) {
            throw new DonneesInvalidesException("Le stock ne peut pas être négatif");
        }
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return String.format("- %s | %s | %d cts | stock=%d", id, libelle, prixCentimes, stock);
    }
}