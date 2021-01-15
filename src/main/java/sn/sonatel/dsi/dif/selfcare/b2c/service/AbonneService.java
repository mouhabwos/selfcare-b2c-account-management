package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.InfoClientWrapper;

import java.util.Set;

public interface AbonneService {

    AbonneDTO getInformationAbonne(String msisdn);

    boolean isOrangeNumber(String msisdn);

    InfoClientWrapper getInformations(String msisdn);

    IndividualInformation getIndividualInformations(String msisdn);

    boolean isCoorporateNumber(String msisdn);

    Set<String> getMyContactNumbers(String msisdn);
}
