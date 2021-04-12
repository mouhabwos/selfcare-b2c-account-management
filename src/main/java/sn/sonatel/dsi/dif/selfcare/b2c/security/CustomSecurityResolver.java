package sn.sonatel.dsi.dif.selfcare.b2c.security;

import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by centonni on 10/04/19.
 */
@Component("customSecurityResolver")
public class CustomSecurityResolver {

    private final RattachementLigneRepository accountManagementClient;

    public CustomSecurityResolver(RattachementLigneRepository accountManagementClient) {
        this.accountManagementClient = accountManagementClient;
    }

    public boolean isAuthorized(String msisdn){

        return (login().equals(msisdn) || getLignes().contains(msisdn));

    }

    private List<String> getLignes(){

        LinkedList<String> list= new LinkedList<>();
        list.add(login());

        List<RattachementLigne> lignesRattaches = accountManagementClient.findAllByAccountB2CNumero(login());

        if (!lignesRattaches.isEmpty()){

            lignesRattaches.forEach( d -> list.add(d.getNumero()));

        }

        return list;
    }

    private String login(){
        return SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT);
    }
}
