package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;

public interface AbonneService {

    IndividualInformation getIndividualInformation(String msisdn);

    OrganizationInformation getOrganizationInformation(String msisdn);
}
