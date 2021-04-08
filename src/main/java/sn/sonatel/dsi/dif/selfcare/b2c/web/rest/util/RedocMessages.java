package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

public final class RedocMessages {

    private RedocMessages() {
        //Defaul constructor
    }

    public static final class Abonne {

        public static final String DESCRIPTION_IS_POSPAID_NUMBER = "Cette api permet de savoir si le numéro saisie est un PostPaid ou non";
        public static final String DESCRIPTION_CUSTOMEROFFER_NUMBER = "Cette api permet de récupérer la souscription de l'utilisateur";
        public static final String DESCRIPTION_IS_ORGANIZATION_NUMBER = "Cette api permet de savoir si le numéro saisie est un numéro d'entreprise ou pas";
        public static final String DESCRIPTION_IS_ORANGE_NUMBER ="Cette api permet de savoir si le numéro de téléphone saisie est un numéro orange valide";
        public static final String INFOS_CLIENT ="Cette api permet de récupérer les infos du numéro téléphone saisi";
        public static final String DESCRIPTION_CUSTOMEROFFER_V2_NUMBER = "Cette api permet de récupérer la souscription de l'utilisateur (Dans cette version l'idClient a été masqué pour des raisons de sécurités)";
        public static final String DESCRIPTION_GET_REQUEST_STATUS_BY_ID = "";
        public static final String DESCRIPTION_GET_CONTACT_NUMBERS = "Cette API permet de recuperer toutes les lignes appartenant a un utilisateur";

        public static final String VALUE_MSISDN_DESCRIPTION = "Le numéro de téléphone de l utilisateur";
        public static final String VALUE_MSISDN1_DESCRIPTION = "Le numéro de téléphone de l utilisateur dont on veut savoir si c est un postPaid";

        private Abonne() {
            super ();
        }
    }

    public static final class ExportUsers{

        public static final String DESCRIPTION_EXPORT_ALL_USER = "Cette api permet d exporter la liste de touts les utilisateurs Orange et Moi dans un fichier excel qui sera compressé puis envoyer par mail a l admin connecté";
        public static final String DESCRIPTION_IMPORT_FILE_MSISDN = "Cette api permet de recupere les informations (HashMsisdn, nom et prenom) pour les mettre dans un fichier csv et l'uploader sur FileManager";

        private ExportUsers() {  super (); }
    }
}
