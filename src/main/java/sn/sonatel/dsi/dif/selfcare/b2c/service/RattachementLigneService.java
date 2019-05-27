package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
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

     List<InfoNumberVM> getRattachementLignes(String msisdn);

     RattachementLignesDeleteMultipleVM deleteMultipleRattachementLigne(RattachementLignesDeleteMultipleVM deleteListe);

     ResponseEntity checkNumberFix(CheckNumberFixVM checkNumberFixVM);

    /**
     *
     * @param ligneVM
     * @return rattachementLigne
     *
     * @author Bouya Kande
     * @since 1.1.4
     */
    RattachementLigne addRattachementLigneFixe(RattachementLigneFixeVM ligneVM);

    AccountB2C getAccountB2CByIdClient(String idClient);

}
