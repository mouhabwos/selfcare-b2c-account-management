package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;

public interface AbonneService {

    IndividualInformation getIndividualInformation(String msisdn);
}
