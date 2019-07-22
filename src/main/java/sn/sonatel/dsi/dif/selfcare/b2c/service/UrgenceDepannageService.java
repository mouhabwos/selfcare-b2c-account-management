package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;

import java.io.IOException;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 */
public interface UrgenceDepannageService {

    String ouvertureCompte( OperationDTO operationDTO) throws IOException;

}
