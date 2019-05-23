package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@MappedSuperclass
public class NumeroDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @ApiModelProperty(required = true)
    @NotNull(message = MessageValidation.NUMERO_NON_VIDE)
    @NotBlank(message = MessageValidation.NUMERO_NON_VIDE)
    @Pattern(regexp = Constants.VALIDE_NUMBER_ORANGE_FIXE_MOBILE, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    @Size(min = 9, max = 18, message = MessageValidation.NUMERO_TAILLE_VALIDE)
    @Column(name = "numero", nullable = false)
    protected String numero;

    public NumeroDTO numero(String numero) {
        this.numero = numero;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
