package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 */
public interface UrgenceDepannageService {

    String ouvertureCompte(String operationDTO, MultipartFile formulaire, MultipartFile rectoID,  MultipartFile verso,String canal) throws IOException;

}
