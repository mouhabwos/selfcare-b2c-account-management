package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.InfoClientWrapper;

public interface AbonneService {

    AbonneDTO getInformationAbonne(String msisdn);

    boolean isOrangeNumber(String msisdn);

    InfoClientWrapper getInformations(String msisdn);

    ResponseEntity<String> getBirthDate(String msisdn);

    boolean isCoorporateNumber(String msisdn);
}
