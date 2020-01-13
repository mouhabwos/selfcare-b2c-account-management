package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 *
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 * class OperationDTO
 *
 */
@ToString(doNotUseGetters = true)
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class OperationDTO {

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull(message = MessageValidation.NUMERO_NON_VIDE)
    @NotBlank(message = MessageValidation.NUMERO_NON_VIDE)
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    @Size(min = 9, max = 18, message = MessageValidation.NUMERO_TAILLE_VALIDE)
    private String numero ;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull(message = MessageValidation.LASTNAME_NON_VIDE)
    @ApiModelProperty(required = true)
    @Size(max = 50)
    private  String lastName;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull(message = MessageValidation.FIRSTNAME_NON_VIDE)
    @ApiModelProperty(required = true)
    @Size(max = 50)
    private String firstName;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull(message = MessageValidation.CODE_OPERATION_NOT_NULL)
    @NotBlank(message = MessageValidation.CODE_OPERATION_NOT_VIDE)
    private String operationCode;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private MultipartFile formulaire;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private MultipartFile rectoID;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private MultipartFile verso;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private String email = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private String operationTitre;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private String canal = "ORANGETMOI";


    public void checkFormatPDFFile(String fileName){
        int indexOf = fileName.lastIndexOf('.');
        String extension = fileName.substring(indexOf);
        if(!extension.matches(Constants.PDF_FILE_EXTENSION_REGEX) && !extension.matches(Constants.IMAGE_EXTENSION_REGEX)){
            throw new BadRequestAlertException("Le ficher doit etre un document .pdf ", fileName,"");
        }
    }

    public void checkFormatImageFile(String fileName){
        int indexOf = fileName.lastIndexOf('.');
        String extension = fileName.substring(indexOf);
        if(!extension.matches(Constants.IMAGE_EXTENSION_REGEX)){
            throw new BadRequestAlertException("Le format de l'image doit etre .png, .jpg, .jpeg ", fileName,"");
        }

    }

}
