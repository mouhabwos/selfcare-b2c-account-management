package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.io.Resource;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.*;

public class UserInfoOuvertureCompte {

    @ApiModelProperty(required = true)
    @NotNull(message = "Le numero ne peut pas être vide")
    @NotBlank(message = "Le numéro ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
    @Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
    private String numero;

    @NotNull(message = "Le nom ne peut pas être vide")
    private String lastName;

    @NotNull(message = "Le prénom ne peut pas être vide")
    private String firstName;

    @NotNull(message = "L'opération ne peut pas être vide")
    private String operation;

    @NotNull
    private String operationTitle;

    @NotNull(message = "Le formulaire ne peut pas être vide")
    private String formulaire;

    @NotNull(message = "Le recto ne peut pas être vide")
    private String rectoID;

    private String versoID;

    private Resource objectFormulaire;

    private Resource objectRectoID;

    private Resource objectVersoID;

    @NotNull(message = "L'email ne peut pas être vide")
    @ApiModelProperty(required = true)
    @Email
    @Size(min = 5, max = 254)
    private String email;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(String formulaire) {
        this.formulaire = formulaire;
    }

    public String getRectoID() {
        return rectoID;
    }

    public void setRectoID(String rectoID) {
        this.rectoID = rectoID;
    }

    public String getVersoID() {
        return versoID;
    }

    public void setVersoID(String versoID) {
        this.versoID = versoID;
    }

    public Resource getObjectFormulaire() {
        return objectFormulaire;
    }

    public void setObjectFormulaire(Resource objectFormulaire) {
        this.objectFormulaire = objectFormulaire;
    }

    public Resource getObjectRectoID() {
        return objectRectoID;
    }

    public void setObjectRectoID(Resource objectRectoID) {
        this.objectRectoID = objectRectoID;
    }

    public Resource getObjectVersoID() {
        return objectVersoID;
    }

    public void setObjectVersoID(Resource objectVersoID) {
        this.objectVersoID = objectVersoID;
    }

    public String getOperationTitle() {
        return operationTitle;
    }

    public void setOperationTitle(String operationTitle) {
        this.operationTitle = operationTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
