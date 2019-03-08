package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall.IServiceSOAP;

@RestController
@RequestMapping("/api")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger(RattachementLigneResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementAbonne";

    private final IServiceSOAP iServiceSOAP;

    public AbonneResource(IServiceSOAP iServiceSOAP) {
        this.iServiceSOAP = iServiceSOAP;
    }


}
