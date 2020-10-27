package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.*;

import java.util.List;
import java.util.Optional;


public interface RattachementLigneService {

    RattachementLigne createRattachementLigne(RattachementLigneDTO rattachementLigne);

    RattachementLigne updateRattachementLigne(RattachementLigneDTO rattachementLigne);

    Page<RattachementLigne> getAllRattachementLignes(Pageable pageable);

     Optional<RattachementLigne> getRattachementLigne(Long id);

     void deleteRattachementLigne(Long id);

     RattachementLigne addRattachementLigne(RattachementLigneVM ligneVM);

     List<InfoNumberVM> getRattachementLignes(String msisdn, boolean withCustomerOffer);

     RattachementLignesDeleteMultipleVM deleteMultipleRattachementLigne(RattachementLignesDeleteMultipleVM deleteListe);

    RattachementLigne addRattachementLigneFixe(RattachementLigneFixeVM ligneFixeVM);

}
