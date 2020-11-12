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
        public static final String DESCRIPTION_BIRTHDATE ="Cette api permet de récupérer la date de naissance du numéro téléphone saisie";
        public static final String DESCRIPTION_CUSTOMEROFFER_V2_NUMBER = "Cette api permet de récupérer la souscription de l'utilisateur (Dans cette version l'idClient a été masqué pour des raisons de sécurités)";
        public static final String DESCRIPTION_GET_REQUEST_STATUS_BY_ID = "";

        public static final String VALUE_MSISDN_DESCRIPTION = "Le numéro de téléphone de l utilisateur";
        public static final String VALUE_MSISDN1_DESCRIPTION = "Le numéro de téléphone de l utilisateur dont on veut savoir si c est un postPaid";

        private Abonne() {
            super ();
        }
    }
}
