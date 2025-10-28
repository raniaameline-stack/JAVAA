package src;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class TestProgram {
    public static void main(String[] args) {
        try {
            // Format de date pour créer des dates
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            // Création des commandes
            Commande cmd1 = new Commande(101, sdf.parse("15/01/2024"), "Fournisseur A");
            Commande cmd2 = new Commande(102, sdf.parse("10/01/2024"), "Fournisseur B");
            Commande cmd3 = new Commande(103, sdf.parse("20/01/2024"), "Fournisseur C");
            Commande cmd4 = new Commande(104, sdf.parse("05/01/2024"), "Fournisseur A");
            Commande cmd5 = new Commande(101, sdf.parse("25/01/2024"), "Fournisseur D"); // Doublon numéro

            // Création des clients
            Client client1 = new Client(1, "Mohamed Ali", "Casablanca");
            client1.setTelClient("0612345678");
            
            Client client2 = new Client(2, "Fatima Zahra", "Rabat");
            client2.setTelClient("0623456789");
            
            ClientFidel clientFidel = new ClientFidel(3, "Hassan Karim", "Marrakech", 
                                                    "FID123", 10.0f);
            clientFidel.setTelClient("0634567890");

            // Ajout des commandes aux clients
            client1.enregistrerCommande(cmd1);
            client1.enregistrerCommande(cmd2);
            client2.enregistrerCommande(cmd3);
            clientFidel.enregistrerCommande(cmd4);

            // Utilisation d'une List pour stocker plusieurs clients
            List<Client> listeClients = new ArrayList<>();
            listeClients.add(client1);
            listeClients.add(client2);
            listeClients.add(clientFidel);

            // Tri des commandes d'un client avec Collections.sort()
            System.out.println("=== Avant tri des commandes ===");
            System.out.println(client1);
            
            Collections.sort(client1.getListCommandes());
            System.out.println("\n=== Après tri des commandes par date ===");
            System.out.println(client1);

            // Utilisation d'un Set pour éviter les doublons
            Set<Commande> setCommandes = new HashSet<>();
            setCommandes.add(cmd1);
            setCommandes.add(cmd2);
            setCommandes.add(cmd5); // Cette commande a le même numéro que cmd1
            
            System.out.println("\n=== Commandes dans le Set (sans doublons) ===");
            for (Commande cmd : setCommandes) {
                System.out.println(cmd);
            }

            // Utilisation d'une Map pour associer chaque client à son codeClient
            Map<Integer, Client> mapClients = new HashMap<>();
            for (Client client : listeClients) {
                mapClients.put(client.getCodeClient(), client);
            }

            System.out.println("\n=== Clients dans la Map ===");
            for (Map.Entry<Integer, Client> entry : mapClients.entrySet()) {
                System.out.println("Code: " + entry.getKey() + " -> " + entry.getValue().getNomClient());
            }

            // Écriture des informations des clients dans un fichier texte
            ecrireClientsDansFichier(listeClients, "clients.txt");

            // Lecture du fichier et affichage dans la console
            System.out.println("\n=== Contenu du fichier clients.txt ===");
            lireFichierClients("clients.txt");

            // Test de suppression de commande
            System.out.println("\n=== Test suppression de commande ===");
            boolean suppressionReussie = client1.supprimerCommande(101);
            System.out.println("Suppression commande 101: " + (suppressionReussie ? "Réussie" : "Échec"));
            System.out.println("Client après suppression: " + client1);

        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour écrire les clients dans un fichier
    public static void ecrireClientsDansFichier(List<Client> clients, String nomFichier) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomFichier))) {
            for (Client client : clients) {
                writer.println(client.toString());
                writer.println("---"); // Séparateur entre clients
            }
            System.out.println("Fichier " + nomFichier + " créé avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier: " + e.getMessage());
        }
    }

    // Méthode pour lire le fichier des clients
    public static void lireFichierClients(String nomFichier) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        }
    }
}