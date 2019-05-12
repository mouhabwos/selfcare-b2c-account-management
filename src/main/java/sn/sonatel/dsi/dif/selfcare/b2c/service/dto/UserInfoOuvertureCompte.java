package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.io.Resource;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.*;

public class UserInfoOuvertureCompte {

    @ApiModelProperty(required = true)
    @NotNull(message = MessageValidation.FIELD_NUMERO)
    @NotBlank(message = MessageValidation.FIELD_NUMERO)
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.FIELD_NUMERO_ORANGE)
    @Size(min = 9, max = 18, message = MessageValidation.FIELD_NUMERO_SIZE)
    private String numero;

    @NotNull(message = MessageValidation.FIELD_LASTNAME)
    private String lastName;

    @NotNull(message = MessageValidation.FIELD_FIRSTNAME)
    private String firstName;

    @NotNull(message = MessageValidation.FIELD_OPERATION)
    private String operation;

    private String operationTitle;

    @NotNull(message = MessageValidation.FIELD_FORMULAIRE)
    private String formulaire;

    @NotNull(message = MessageValidation.FIELD_RECTOID)
    private String rectoID;

    private String versoID;

    private Resource objectFormulaire;

    private Resource objectRectoID;

    private Resource objectVersoID;

    @NotNull(message = MessageValidation.FIELD_EMAIL)
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
