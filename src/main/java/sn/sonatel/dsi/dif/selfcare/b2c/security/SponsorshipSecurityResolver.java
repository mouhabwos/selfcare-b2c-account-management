package sn.sonatel.dsi.dif.selfcare.b2c.security;

import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;

import java.util.LinkedList;
import java.util.List;

@Component("sponsorshipSecurityResolver")
public class SponsorshipSecurityResolver {

    private final SponsorRepository sponsorRepository;

    public SponsorshipSecurityResolver(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    public boolean isSponsor(String msisdn){

        return (getListSponsorNumber().contains(msisdn));

    }

    private List<String> getListSponsorNumber(){

        LinkedList<String> list= new LinkedList<>();
        List<Sponsor> listSponsor = sponsorRepository.findAll();

        if (!listSponsor.isEmpty()){

            listSponsor.forEach( d -> list.add(d.getMsisdn()));
        }
        return list;
    }
}
