package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

/**
 * class for message field validation
 */
public final class MessageValidation {

    public MessageValidation() {
        // Default constructor
    }

    public static final String FIELD_NUMERO = "Le numero ne peut pas être vide";

    public static final String FIELD_NUMERO_ORANGE = "Le numéro doit un numéro orange valide";

    public static final String FIELD_NUMERO_SIZE = "La taille du numéro doit être de 9 chiffres";

    public static final String FIELD_FIRSTNAME = "Le prénom ne peut pas être vide";

    public static final String FIELD_LASTNAME = "Le nom ne peut pas être vide";

    public static final String FIELD_EMAIL_VALID = "L'email doit être une adresse email bien formée";

    public static final String FIELD_EMAIL = "L'email ne peut pas être vide";

    public static final String FIELD_OPERATION = "L'opération ne peut pas être vide";

    public static final String FIELD_FORMULAIRE = "Le formulaire ne peut pas être vide";

    public static final String FIELD_RECTOID = "Le recto ne peut pas être vide";

    public static final String FIELD_CODEOTP = "Le code ne peut pas être vide";

    public static final String FIELD_CODEOTP_SIZE = "La taille du code doit être de 6 chiffres";

    public static final String FIELD_TYPE_NUMERO = "Le type de numéro ne peut pas être vide";
}
