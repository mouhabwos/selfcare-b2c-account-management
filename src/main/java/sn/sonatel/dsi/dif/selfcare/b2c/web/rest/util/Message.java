package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

public final class Message {

    private Message() {
        super();
    }

    public static final class Abonne {

        public static final String IS_POSPAID = "Verifier si le numero est pospaid";
        public static final String SOUSC_USER = "Recuperation de la souscription de l abonne";
        public static final String INFO_ABONNE = "Recuperation des informations de l abonne";
        public static final String CUSTOMEROFFER = "Recuperation du custommer offer de l abonne";
        public static final String INFOS_CLIENT = "Recuperation des infos de l abonne";
        public static final String IS_ORANGE_NUMBER = "Verifier si le numero est un numero orange";
        public static final String GET_REQUEST_STATUS_BY_ID = "Recuperation Statut d une demande par son id";
        public static final String GET_REQUEST_STATUS_BY_MSISDN = "Recuperation Statut d une demande par le numero";
        public static final String IS_ORGANIZATION_NUMBER = "Verifier si le numero est un numero d entreprise";
        public static final String CONTACT_NUMBERS = "Recuperation de la liste des numero identifiees d un abonne par le msisdn";
        public static final String STATUS_MSISDN = "Recuperation du statut d un numero";
        public static final String CLIENT_TROUBLE = "Envoi de réclamation ou du dérangement client au service client par mail";

        private Abonne() {
            super();
        }
    }

    public static final class Authentification {

        public static final String CONN_SUCC = "Connexion Reussie";
        public static final String CONN_ERR = "Erreur de Connexion";

        private Authentification() {
            super();
        }
    }

    public static final class AccountB2C {

        public static final String CREATE_ACCOUNT = "Creation d un nouveau compte";
        public static final String ENREGISTREMENT_COMPTE = "enregistrement compte";
        public static final String UPDATE = "Modification d un compte";
        public static final String LIST_ACCOUNT = "Liste des comptes";
        public static final String LIST_ACCOUNT_BY_ID = "Affichage d une categorie Pass Illimix";
        public static final String DELETE_ACCOUNT = "Suppression d une categorie Pass Illimix";
        public static final String SEARCH_ACCOUNT = "Suppression d une categorie Pass Illimix";
        public static final String CHECK_NUMBER = "Suppression d une categorie Pass Illimix";
        public static final String GET_ACCOUNT = "Suppression d une categorie Pass Illimix";

        private AccountB2C() {
            super();
        }
    }

    public static final class Rattachement {

        public static final String ADD = "Creation de rattachement de ligne";
        public static final String UPDATE = "Modification de ligne rattachee";
        public static final String LIST = "Lister les  lignes rattachées";
        public static final String LIST_BY_ID = "Lister une ligne rattachee";
        public static final String DELETE = "Suppression de lignes rattachees de ligne mobile";

        public static final String DELETE_ALL = "Suppression multiple de ligne rattachee";

        public static final String SAVE = "Rattachement de ligne";
        public static final String List_By_MSISDN = "Recuperation de la liste de numeros rattaches en fonction du login";

        public static final String SEARCH = "Rechercher une ligne rattachee";

        public static final String CHECK_NUMBER_FIXE = "Verification de la disponibilite du numero fix";

        public static final String ADD_LIGNE_FIXE = "Rattachement de ligne fixe via l ID CLIENT";

        public static final String SAVE_RATTACHEMENT_LIGNE_BY_CNI = "Rattachement de ligne ";
        public static final String SAVE_RATTACHEMENT_LIGNE_BY_OTP = "Rattachement de ligne  par code OTP";

        private Rattachement() {
            super();
        }
    }

    public static final class Account {

        public static final String ADD = "Creation de compte B2C";
        public static final String UPDATE = "Modification de compte B2C";
        public static final String UPDATE_EXPLOITANT = "Modification de compte B2C par un compte exploitant";
        public static final String LIST = "Recuperation de la Lister des comptes B2C crees";
        public static final String LIST_BY_ID = "Recuperation d un compte B2C par id";
        public static final String DELETE = "Suppression d'un compte utilisateur";

        public static final String DELETE_ALL = "Suppression multiple de ligne rattachee";

        public static final String SAVE = "Ajout de rattachement de ligne";
        public static final String List_By_MSISDN = "Affichage d une ligne rattachee";

        public static final String SEARCH = "Rechercher un compte";

        public static final String CHECK_Numero = "Verification de la disponibilite du numero ";
        public static final String CHECK_Email = "Verification de la disponibilite de l'email ";

        public static final String GET_ACCOUNT = "Recuperation d un compte a partir du numero de telephone ";

        public static final String UPDATE_TUTORIAL_VIEW = "Mise a jour du champs tutorialView a true";

        public static final String TUTORIAL_VIEW_STATUS = "Verifier si le tutoriel a ete vu par l'utilisateur";

        public static final String OUVERTURE_COMPTE = "Operation d ouverture de compte dans urgence et depannage";

        public static final String STATUS_MAIL = "Verification de l envoie du mail";
        public static final String RESET_PASSWORD = "Reinitialisation du mot de passe en mode lite";
        public static final String LOGIN = "Login du compte utilisateur en mode lite";

        private Account() {
            super();
        }
    }

    public static final class Sponsee {

        public static final String CREATE_SPONSEE = "Parrainage d un nouveau numero";
        public static final String UPDATE_SPONSEE = "Modification d un sponsee";
        public static final String LIST_SPONSEE = "Liste des sponsee";
        public static final String SPONSEE_BY_ID = "Affichage d un sponsee par l id";
        public static final String DELETE_SPONSEE = "Suppression d un sponsee";
        public static final String SMS_TO_SPONSEE = "Envoie sms au sponsee";
        public static final String LIST_SPONSEE_BY_MSISDN = "Liste des sponsee par msisdn du sponsor";
        public static final String CHECK_SPONSEE = "Verification du numero a parraine";

        private Sponsee() {
            super();
        }
    }

    public static final class Sponsor {

        public static final String CREATE = "Création d'un sponsor";
        public static final String UPDATE = "Modification d un sponsor";
        public static final String LIST = "Liste des sponsors";
        public static final String SPONSOR_BY_ID = "Affichage d un sponsor par l id";
        public static final String CHECK_SPONSOR = "Verification du numero d un sponsor";
        public static final String UPLOAD = "Importation d'un fichiers sponsors";
        public static final String DELETE = "Suppression d un sponsor";

        private Sponsor() {
            super();
        }
    }

    public static final class NotificationInformation {

        public static final String MSISDN_FIREBASEID_BY_CODE_FORMULE = "Liste des msisdn avec les firebaseId par le codeFormule";

        public static final String UPDATE_CODE_FORMULE_BY_MSISDN = "Update code formule";

        public static final String GET_FIREBASEID_BY_MSISDN = "Recuperation du firebaseId pour une liste de numero";

        public static final String REGISTER = "add information Notification";

        public static final String MSISDN_FIREBASEID_BY_LIST_CODE_FORMULE =
            "Liste des msisdn avec les firebaseId pour une liste de codeFormule";

        private NotificationInformation() {
            super();
        }
    }

    public static final class ExportUsers {

        public static final String EXPORT_ALL_USER = "Exporter la liste de touts les utilisateurs Orange et Moi dans un fichier excel";
        public static final String IMPORT_FILE_MSISDN = "Upload des informations des utilisations via une liste de numero";
        public static final String UPLOADED_FILE_INFORMATION = "Recuperation de la liste des informations des fichiers uploade";

        private ExportUsers() {
            super();
        }
    }
}
