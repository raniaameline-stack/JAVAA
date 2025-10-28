package src;

public class LignePanier {
    private Article article;
    private int quantite;
    
    public LignePanier(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }
    
    public Article getArticle() { return article; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public int getSousTotal() {
        return article.getPrixCentimes() * quantite;
    }
    
    @Override
    public String toString() {
        return String.format("- %s x %d => %d cts", 
            article.getId(), quantite, getSousTotal());
    }
}