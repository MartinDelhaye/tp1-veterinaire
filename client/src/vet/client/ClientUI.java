package vet.client;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import vet.client.ClientService.PatientInfo;

public class ClientUI {

    private final ClientService service;
    private final Scanner scanner = new Scanner(System.in);

    public ClientUI(ClientService service) {
        this.service = service;
    }

    public void lancer() {
        try {
            boolean continuer = true;
            while (continuer) {
                afficherMenu();
                int choix = lireEntierValide("Choix : ");
                switch (choix) {
                    case 1 -> listerPatients();
                    case 2 -> rechercherPatient();
                    case 3 -> creerPatient();
                    case 4 -> sAbonner();
                    case 5 -> continuer = false;
                    case 6 -> creerPatientsDemonstration();
                    default -> System.out.println(Color.error("Choix invalide."));
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println();
        } finally {
            quitter();
        }
    }

    private void afficherMenu() {
        System.out.println(Color.title("------------ Menu ------------"));
        System.out.println("1. Consulter la liste des patients");
        System.out.println("2. Consulter les informations d'un patient");
        System.out.println("3. Créer un patient");
        System.out.println(service.estAbonne()
                ? "4. Se désabonner des alertes du cabinet"
                : "4. S'abonner aux alertes du cabinet");
        System.out.println("5. Quitter");
        System.out.println(Color.info("[demonstration] 6. Créer 100 patients"));
    }

    // ---------- Actions du menu principal ----------

    private void listerPatients() {
        try {
            List<PatientInfo> patients = service.listerPatients();
            System.out.println("Liste des patients :");
            int i = 1;
            for (PatientInfo p : patients) {
                System.out.println("Patient n°" + i++ + ". " + p.nom() + " (Propriétaire : " + p.proprietaire() + ")");
            }
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    private void rechercherPatient() {
        String nom = lireNonVide("Nom du patient : ");
        try {
            PatientInfo p = service.rechercherPatient(nom);
            System.out.println(Color.success("Patient trouvé : " + p.nom()));
            System.out.println(Color.success("Propriétaire : " + p.proprietaire()));
            System.out.println(Color.success("Race : " + p.race()));
            System.out.println(Color.success("Espèce : " + p.espece()));
            gererDossier(p.nom());
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    private void creerPatient() {
        String nom = lireNonVide("Nom du patient : ");
        String proprietaire = lireNonVide("Nom du propriétaire : ");
        String race = lireNonVide("Race du patient : ");
        String espece = lireNonVide("Nom de l'espèce : ");
        int vieMoyenne = lireEntierValide("Durée de vie moyenne de l'espèce : ");
        try {
            PatientInfo p = service.creerPatient(nom, proprietaire, race, espece, vieMoyenne);
            System.out.println(Color.success("Patient créé : " + p.nom()));
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    private void sAbonner() {
        try {
            if (service.estAbonne()) {
                service.seDesabonner();
                System.out.println(Color.success("Vous êtes désabonné des alertes du cabinet."));
            } else {
                service.sAbonner();
                System.out.println(Color.success("Vous êtes abonné aux alertes du cabinet."));
            }
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    /** Appelée une seule fois, en sortie de lancer(), même après Ctrl+D. */
    private void quitter() {
        if (service.estAbonne()) {
            try {
                service.seDesabonner();
            } catch (ClientException e) {
                System.out.println(Color.error("Désabonnement impossible (" + e.getMessage() + "), fermeture quand même."));
            }
        }
        System.out.println(Color.info("Au revoir !"));
    }

    private void creerPatientsDemonstration() {
        try {
            System.out.println(Color.info("Création de 100 patients de démonstration..."));
            service.creerPatientsEnMasse(100);
            System.out.println(Color.success("Terminé."));
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    // ---------- Menu du dossier ----------

    private void gererDossier(String nom) {
        boolean retour = false;
        while (!retour) {
            System.out.println(Color.title("--- Dossier de " + nom + " ---"));
            try {
                String etat = service.consulterEtatSante(nom);
                System.out.println("État de santé : " + (etat == null ? "(non renseigné)" : etat));
            } catch (ClientException e) {
                System.out.println(Color.error(e.getMessage()));
                return;
            }
            System.out.println("1. Modifier l'état de santé");
            System.out.println("2. Voir l'historique des observations");
            System.out.println("3. Ajouter une observation");
            System.out.println("4. Retour au menu principal");
            int choix = lireEntierValide("Choix : ");
            switch (choix) {
                case 1 -> modifierEtatSante(nom);
                case 2 -> voirObservations(nom);
                case 3 -> ajouterObservation(nom);
                case 4 -> retour = true;
                default -> System.out.println(Color.error("Choix invalide."));
            }
        }
    }

    private void modifierEtatSante(String nom) {
        String nouvelEtat = lireNonVide("Nouvel état de santé : ");
        try {
            service.modifierEtatSante(nom, nouvelEtat);
            System.out.println(Color.success("État de santé mis à jour."));
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    private void voirObservations(String nom) {
        try {
            List<String> observations = service.consulterObservations(nom);
            if (observations.isEmpty()) {
                System.out.println("Aucune observation enregistrée.");
            } else {
                System.out.println("Historique des observations :");
                observations.forEach(o -> System.out.println("  " + o));
            }
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    private void ajouterObservation(String nom) {
        String texte = lireNonVide("Nouvelle observation : ");
        try {
            service.ajouterObservation(nom, texte);
            System.out.println(Color.success("Observation ajoutée."));
        } catch (ClientException e) {
            System.out.println(Color.error(e.getMessage()));
        }
    }

    // ---------- Saisies ----------

    private String lireNonVide(String prompt) {
        while (true) {
            System.out.print(prompt);
            String saisie = scanner.nextLine().trim();
            if (!saisie.isEmpty()) {
                return saisie;
            }
            System.out.println(Color.error("Saisie vide, recommencez."));
        }
    }

    private int lireEntierValide(String prompt) {
        while (true) {
            System.out.print(prompt);
            String saisie = scanner.nextLine();
            try {
                return Integer.parseInt(saisie.trim());
            } catch (NumberFormatException e) {
                System.out.println(Color.error("Veuillez entrer un entier valide."));
            }
        }
    }
}
