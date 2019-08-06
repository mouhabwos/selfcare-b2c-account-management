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

    public static final String MESSAGE_NON_VIDE = "Le message ne doit pas être vide";

    public static final String MOTE_DE_PASSE_NON_VIDE = "Le mot de passe ne doit pas être vide";

    public static final String MOTE_DE_PASSE_TAILLE_VALIDE = "Le password doit contenir au moins 8 caracteres";

    public static final String MOTE_DE_PASSE_PATTERN_VALIDE = "Le mot de passe saisi est incorrect";

    public static final String CODE_OPERATION_NOT_NULL = "Le Code de l'opération ne peux pas etre null";

    public static final String CODE_OPERATION_NOT_VIDE = "Le Code de l'opération ne peux pas etre vide";

    public static final String RECTOID_NOT_VIDE = "Le rectoID ne peux pas etre vide";

    public static final String VALIDE_FORMULAIRE = "Ce fichier n'est pas un fichier pdf valide";

    public static final String VALIDE_RECTO = " Fichier c'est pas valide";

    public static final String VALIDE_VERSO = " Fichier c'est pas valide";
}
