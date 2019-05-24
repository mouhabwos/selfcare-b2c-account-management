package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

/**
 * class for message field validation
 */
public final class MessageValidation {

    private MessageValidation() {
        // Default constructor
    }

    public static final String NUMERO_NON_VIDE = "Le numero ne peut pas être vide";

    public static final String NUMERO_ORANGE_VALIDE = "Le numéro doit un numéro orange valide";

    public static final String NUMERO_TAILLE_VALIDE = "La taille du numéro doit être de 9 chiffres";

    public static final String FIRSTNAME_NON_VIDE = "Le prénom ne peut pas être vide";

    public static final String LASTNAME_NON_VIDE = "Le nom ne peut pas être vide";

    public static final String EMAIL_VALID = "L'email doit être une adresse email bien formée";

    public static final String EMAIL_NON_VIDE = "L'email ne peut pas être vide";

    public static final String OPERATION_NON_VIDE = "L'opération ne peut pas être vide";

    public static final String FORMULAIRE_NON_VIDE = "Le formulaire ne peut pas être vide";

    public static final String RECTO_ID_NON_VIDE = "Le recto ne peut pas être vide";

    public static final String CODE_OTP_NON_VIDE = "Le code ne peut pas être vide";

    public static final String CODE_OTP_TAILLE_VALIDE = "La taille du code doit être de 6 chiffres";

    public static final String TYPE_NUMERO_NON_VIDE = "Le type de numéro ne peut pas être vide";

    public static final String ID_CLIENT = "L'id client ne peut pas être vide.";
}
