package src;

public class KendiFood {
    private Catalogue catalogue;
    private Panier panier;
    private CodePromo[] codesPromo;
    private int tailleCodes;
    
    public KendiFood() {
        this.catalogue = new Catalogue();
        this.panier = new Panier();
        this.codesPromo = new CodePromo[10];
        this.tailleCodes = 0;
        initialiserCodesPromo();
    }
    
    private void initialiserCodesPromo() {
        try {
            ajouterCodePromo(new CodePromo("KENDI10", 10));
            ajouterCodePromo(new CodePromo("KENDI20", 20));
            ajouterCodePromo(new CodePromo("KENDI30", 30));
        } catch (DonneesInvalidesException e) {
            System.out.println("Erreur lors de l'initialisation des codes promo: " + e.getMessage());
        }
    }
    
    private void ajouterCodePromo(CodePromo code) {
        if (tailleCodes >= codesPromo.length) {
            // Redimensionner le tableau de codes promo
            CodePromo[] nouveauTableau = new CodePromo[codesPromo.length + 5];
            System.arraycopy(codesPromo, 0, nouveauTableau, 0, tailleCodes);
            codesPromo = nouveauTableau;
        }
        codesPromo[tailleCodes] = code;
        tailleCodes++;
    }
    
    public void executer() {
        System.out.println("=== KendiFood - Système de paniers-repas ===");
        
        // Chargement initial du catalogue
        boolean catalogueCharge = false;
        while (!catalogueCharge) {
            try {
                String chemin = SaisieUtilitaire.saisirCheminFichier(
                    "Chemin du fichier catalogue: ");
                catalogue.chargerDepuisFichier(chemin);
                catalogueCharge = true;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Menu principal
        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = SaisieUtilitaire.saisirEntier("Votre choix: ");
            
            try {
                switch (choix) {
                    case 1:
                        catalogue.afficherCatalogue();
                        break;
                    case 2:
                        ajouterAuPanier();
                        break;
                    case 3:
                        supprimerDuPanier();
                        break;
                    case 4:
                        panier.afficherPanier();
                        break;
                    case 5:
                        genererRecu();
                        break;
                    case 6:
                        sauvegarderCatalogue();
                        break;
                    case 0:
                        continuer = false;
                        System.out.println("Merci d'avoir utilisé KendiFood !");
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (KendiFoodException e) {
                System.out.println("Erreur: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erreur inattendue: " + e.getMessage());
            }
            System.out.println();
        }
    }
    
    private void afficherMenu() {
        System.out.println("\n--- Menu KendiFood ---");
        System.out.println("1. Afficher le catalogue");
        System.out.println("2. Ajouter un article au panier");
        System.out.println("3. Supprimer un article du panier");
        System.out.println("4. Afficher le panier");
        System.out.println("5. Générer un reçu");
        System.out.println("6. Sauvegarder le catalogue");
        System.out.println("0. Quitter");
    }
    
    private void ajouterAuPanier() throws KendiFoodException {
        catalogue.afficherCatalogue();
        String id = SaisieUtilitaire.saisirString("ID de l'article à ajouter: ");
        int qte = SaisieUtilitaire.saisirEntier("Quantité: ");
        
        panier.ajouterAuPanier(id, qte, catalogue);
        System.out.println("Article ajouté au panier avec succès.");
    }
    
    private void supprimerDuPanier() {
        panier.afficherPanier();
        if (panier.getTailleCourante() == 0) {
            return;
        }
        
        String id = SaisieUtilitaire.saisirString("ID de l'article à supprimer: ");
        if (panier.supprimerDuPanier(id)) {
            System.out.println("Article supprimé du panier avec succès.");
        } else {
            System.out.println("Article non trouvé dans le panier.");
        }
    }
    
    private void genererRecu() {
        String code = SaisieUtilitaire.saisirString("Code promo (laisser vide si aucun): ");
        if (code.isEmpty()) {
            code = null;
        }
        
        String recu = panier.genererRecu(code, codesPromo, tailleCodes);
        System.out.println(recu);
    }
    
    private void sauvegarderCatalogue() {
        String chemin = SaisieUtilitaire.saisirString("Chemin de sauvegarde: ");
        try {
            catalogue.sauvegarderVersFichier(chemin);
        } catch (RuntimeException e) {
            System.out.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        KendiFood app = new KendiFood();
        app.executer();
    }
}