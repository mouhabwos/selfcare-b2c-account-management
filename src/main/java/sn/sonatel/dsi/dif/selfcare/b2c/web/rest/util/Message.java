package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

public final class Message {

    private Message() {
        super ();
    }

    public static final class Abonne {

        public static final String SOUSC_USER = "Recuperation de la souscription de l'abonne";
        public static final String INFO_ABONNE = "Affichage information de l'abonne";

        private Abonne() {
            super ();
        }
    }

    public static final class Authentification {

        public static final String CONN_SUCC = "Connexion Réussie";
        public static final String CONN_ERR = "Erreur de Connexion";

        private Authentification() {
            super ();
        }
    }

    public static final class AccountB2C {

        public static final String CREATE_ACCOUNT = "Creation d'un nouveau compte";
        public static final String ENREGISTREMENT_COMPTE = "enregistrement compte";
        public static final String UPDATE = "Modification d'un compte";
        public static final String LIST_ACCOUNT = "Liste des comptes";
        public static final String LIST_ACCOUNT_BY_ID = "Affichage d'une categorie Pass Illimix";
        public static final String DELETE_ACCOUNT = "Suppression d'une categorie Pass Illimix";
        public static final String SEARCH_ACCOUNT = "Suppression d'une categorie Pass Illimix";
        public static final String CHECK_NUMBER = "Suppression d'une categorie Pass Illimix";
        public static final String GET_ACCOUNT = "Suppression d'une categorie Pass Illimix";

        private AccountB2C() {
            super ();
        }
    }

    public static final class Rattachement {

        public static final String ADD = "Creation de rattachement de ligne";
        public static final String UPDATE = "Modification de ligne rattachée";
        public static final String LIST = "Lister les  lignes rattachées";
        public static final String LIST_BY_ID = "Lister une ligne rattachée";
        public static final String DELETE = "Suppression de lignes rattachées de ligne mobile";

        public static final String DELETE_ALL = "Suppression multiple de ligne rattachée";

        public static final String SAVE = "Rattachement de ligne mobile";
        public static final String List_By_MSISDN = "Affichage d'une ligne rattachée";

        public static final String SEARCH = "Rechercher une ligne rattachée";


        private Rattachement() {
            super ();
        }
    }


    public static final class Account {

        public static final String ADD = "Creation de compte";
        public static final String UPDATE = "Modification de compte";
        public static final String LIST = "Lister les  comptes créés";
        public static final String LIST_BY_ID = "Lister un compte";
        public static final String DELETE = "Suppression d'un compte utilisateur";

        public static final String DELETE_ALL = "Suppression multiple de ligne rattachée";

        public static final String SAVE = "Ajout de rattachement de ligne";
        public static final String List_By_MSISDN = "Affichage d'une ligne rattachée";

        public static final String SEARCH = "Rechercher un compte";

        public static final String CHECK_Numero = "Tester existance du numero ";
        public static final String CHECK_Email = "Tester existance du numero ";

        public static final String Authent = "Auhtentification  ";


        private Account() {
            super ();
        }
    }


}
