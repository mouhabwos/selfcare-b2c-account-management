package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;

public interface AbonneService {

    AbonneDTO getInformationAbonne(String msisdn);

    boolean isOrangeNumber(String msisdn);
}
