package src;

public class Panier {
    private LignePanier[] lignes;
    private int tailleCourante;
    private int capacite;
    
    public Panier() {
        this.capacite = 5;
        this.lignes = new LignePanier[capacite];
        this.tailleCourante = 0;
    }
    
    public void ajouterAuPanier(String id, int qte, Catalogue catalogue) throws KendiFoodException {
        int indexArticle = catalogue.trouverIndexParId(id);
        if (indexArticle == -1) {
            throw new DonneesInvalidesException("Article non trouvé: " + id);
        }
        
        Article article = catalogue.getArticle(indexArticle);
        if (qte > article.getStock()) {
            throw new StockInsuffisantException(id, article.getStock());
        }
        
        // Vérifier si l'article est déjà dans le panier
        int indexPanier = trouverIndexDansPanier(id);
        if (indexPanier != -1) {
            // Mettre à jour la quantité existante
            LignePanier ligne = lignes[indexPanier];
            int nouvelleQte = ligne.getQuantite() + qte;
            if (nouvelleQte > article.getStock() + ligne.getQuantite()) {
                throw new StockInsuffisantException(id, article.getStock());
            }
            ligne.setQuantite(nouvelleQte);
        } else {
            // Ajouter une nouvelle ligne
            agrandirPanierSiNecessaire();
            lignes[tailleCourante] = new LignePanier(article, qte);
            tailleCourante++;
        }
        
        // Décrémenter le stock
        try {
            article.setStock(article.getStock() - qte);
        } catch (DonneesInvalidesException e) {
            throw new KendiFoodException("Erreur de mise à jour du stock: " + e.getMessage());
        }
    }
    
    public boolean supprimerDuPanier(String id) {
        int index = trouverIndexDansPanier(id);
        if (index == -1) {
            return false;
        }
        
        // Restaurer le stock
        LignePanier ligne = lignes[index];
        Article article = ligne.getArticle();
        try {
            article.setStock(article.getStock() + ligne.getQuantite());
        } catch (DonneesInvalidesException e) {
            System.out.println("Erreur lors de la restauration du stock: " + e.getMessage());
        }
        
        // Décaler les éléments pour combler le trou
        for (int i = index; i < tailleCourante - 1; i++) {
            lignes[i] = lignes[i + 1];
        }
        lignes[tailleCourante - 1] = null;
        tailleCourante--;
        
        return true;
    }
    
    private int trouverIndexDansPanier(String id) {
        for (int i = 0; i < tailleCourante; i++) {
            if (lignes[i].getArticle().getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
    
    private void agrandirPanierSiNecessaire() {
        if (tailleCourante >= capacite) {
            capacite += 5;
            LignePanier[] nouveauTableau = new LignePanier[capacite];
            System.arraycopy(lignes, 0, nouveauTableau, 0, tailleCourante);
            lignes = nouveauTableau;
        }
    }
    
    public int totalBrut() {
        int total = 0;
        for (int i = 0; i < tailleCourante; i++) {
            total += lignes[i].getSousTotal();
        }
        return total;
    }
    
    public void afficherPanier() {
        System.out.println("[PANIER]");
        if (tailleCourante == 0) {
            System.out.println("Le panier est vide");
            return;
        }
        
        // Trouver la longueur maximale pour l'alignement
        int maxIdLength = 0;
        int maxPrixLength = 0;
        
        for (int i = 0; i < tailleCourante; i++) {
            LignePanier ligne = lignes[i];
            maxIdLength = Math.max(maxIdLength, ligne.getArticle().getId().length());
            maxPrixLength = Math.max(maxPrixLength, 
                String.valueOf(ligne.getSousTotal()).length());
        }
        
        for (int i = 0; i < tailleCourante; i++) {
            LignePanier ligne = lignes[i];
            String id = String.format("%-" + (maxIdLength + 2) + "s", 
                ligne.getArticle().getId() + " x" + ligne.getQuantite());
            String prix = String.format("%" + maxPrixLength + "d cts", ligne.getSousTotal());
            System.out.println("- " + id + " => " + prix);
        }
        
        System.out.println("Total brut : " + totalBrut() + " cts");
    }
    
    public String genererRecu(String codeOptionnel, CodePromo[] codesPromo, int tailleCodes) {
        StringBuilder recu = new StringBuilder();
        
        recu.append("===== REÇU KendiFood =====\n");
        
        if (tailleCourante == 0) {
            recu.append("Le panier est vide\n");
            return recu.toString();
        }
        
        // Trouver les longueurs maximales pour l'alignement
        int maxIdLength = 0;
        int maxPrixLength = 0;
        
        for (int i = 0; i < tailleCourante; i++) {
            LignePanier ligne = lignes[i];
            maxIdLength = Math.max(maxIdLength, ligne.getArticle().getId().length());
            maxPrixLength = Math.max(maxPrixLength, 
                String.valueOf(ligne.getSousTotal()).length());
        }
        
        // Détail des lignes
        for (int i = 0; i < tailleCourante; i++) {
            LignePanier ligne = lignes[i];
            String id = String.format("%-" + (maxIdLength + 2) + "s", 
                ligne.getArticle().getId() + " x" + ligne.getQuantite());
            String prix = String.format("%" + maxPrixLength + "d cts", ligne.getSousTotal());
            recu.append("- ").append(id).append(" -> ").append(prix).append("\n");
        }
        
        int totalBrut = totalBrut();
        recu.append("Total brut : ").append(totalBrut).append(" cts\n");
        
        // Application du code promo si fourni
        if (codeOptionnel != null && !codeOptionnel.trim().isEmpty()) {
            try {
                int totalNet = appliquerCode(codeOptionnel, totalBrut, codesPromo, tailleCodes);
                int reduction = totalBrut - totalNet;
                recu.append("Code appliqué : ").append(codeOptionnel)
                    .append(" (-").append(reduction).append(" cts)\n");
                recu.append("Total à payer : ").append(totalNet).append(" cts\n");
            } catch (CodePromoInconnuException e) {
                recu.append("Code inconnu : ").append(codeOptionnel).append("\n");
                recu.append("Total à payer : ").append(totalBrut).append(" cts\n");
            }
        } else {
            recu.append("Total à payer : ").append(totalBrut).append(" cts\n");
        }
        
        return recu.toString();
    }
    
    public int appliquerCode(String code, int totalBrut, CodePromo[] codesPromo, int tailleCodes) 
            throws CodePromoInconnuException {
        for (int i = 0; i < tailleCodes; i++) {
            if (codesPromo[i].getCode().equals(code)) {
                int pourcentage = codesPromo[i].getPourcentage();
                return totalBrut - (totalBrut * pourcentage / 100);
            }
        }
        throw new CodePromoInconnuException(code);
    }
    
    public int getTailleCourante() {
        return tailleCourante;
    }
}