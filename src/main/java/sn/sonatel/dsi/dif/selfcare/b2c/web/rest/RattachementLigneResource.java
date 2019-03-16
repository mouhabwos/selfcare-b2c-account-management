package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;
import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.search.RattachementLigneSearchRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSelfcareb2cSOAP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneNotFoundException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LoginAlreadyUsedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.InfoNumberVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLignesDeleteMultipleVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RattachementLigne.
 */
@RestController
@RequestMapping("/api")
public class RattachementLigneResource {

    private final Logger log = LoggerFactory.getLogger(RattachementLigneResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementRattachementLigne";

    private final RattachementLigneRepository rattachementLigneRepository;

    private final RattachementLigneSearchRepository rattachementLigneSearchRepository;

    private final AccountB2CRepository accountB2CRepository;

    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;


    public RattachementLigneResource(RattachementLigneRepository rattachementLigneRepository, RattachementLigneSearchRepository rattachementLigneSearchRepository, AccountB2CRepository accountB2CRepository, @Qualifier("loadBalancedRestTemplate")RestTemplate restTemplate) {
        this.rattachementLigneRepository = rattachementLigneRepository;
        this.rattachementLigneSearchRepository = rattachementLigneSearchRepository;
        this.accountB2CRepository = accountB2CRepository;

        this.restTemplate = restTemplate;
    }

    /**
     * POST  /rattachement-lignes : Create a new rattachementLigne.
     *
     * @param rattachementLigne the rattachementLigne to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rattachementLigne, or with status 400 (Bad Request) if the rattachementLigne has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rattachement-lignes")
    public ResponseEntity<RattachementLigne> createRattachementLigne(@Valid @RequestBody RattachementLigneDTO rattachementLigne) throws URISyntaxException {
        log.debug("REST request to save RattachementLigne : {}", rattachementLigne);
        if (rattachementLigne.getId() != null) {
            throw new BadRequestAlertException("A new rattachementLigne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(rattachementLigne.getTypeNumero());
        ligne.setAccountB2C(rattachementLigne.getAccountB2C());
        ligne.setNumero(rattachementLigne.getNumero());
        ligne.setStatut(rattachementLigne.getStatut());
        ligne.setCodeVerification(rattachementLigne.getCodeVerification());
        ligne.setTypeVerification(rattachementLigne.getTypeVerification());

        RattachementLigne result = rattachementLigneRepository.save(ligne);
        rattachementLigneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rattachement-lignes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rattachement-lignes : Updates an existing rattachementLigne.
     *
     * @param rattachementLigne the rattachementLigne to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rattachementLigne,
     * or with status 400 (Bad Request) if the rattachementLigne is not valid,
     * or with status 500 (Internal Server Error) if the rattachementLigne couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rattachement-lignes")
    public ResponseEntity<RattachementLigne> updateRattachementLigne(@Valid @RequestBody RattachementLigneDTO rattachementLigne) throws URISyntaxException {
        log.debug("REST request to update RattachementLigne : {}", rattachementLigne);
        if (rattachementLigne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RattachementLigne ligne = new RattachementLigne();
        ligne.setId(rattachementLigne.getId());
        ligne.setTypeNumero(rattachementLigne.getTypeNumero());
        ligne.setAccountB2C(rattachementLigne.getAccountB2C());
        ligne.setNumero(rattachementLigne.getNumero());
        ligne.setStatut(rattachementLigne.getStatut());
        ligne.setCodeVerification(rattachementLigne.getCodeVerification());
        ligne.setTypeVerification(rattachementLigne.getTypeVerification());

        RattachementLigne result = rattachementLigneRepository.save(ligne);
        rattachementLigneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rattachementLigne.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rattachement-lignes : get all the rattachementLignes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rattachementLignes in body
     */
    @GetMapping("/rattachement-lignes")
    public ResponseEntity<List<RattachementLigne>> getAllRattachementLignes(Pageable pageable) {
        log.debug("REST request to get a page of RattachementLignes");
        Page<RattachementLigne> page = rattachementLigneRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rattachement-lignes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /rattachement-lignes/:id : get the "id" rattachementLigne.
     *
     * @param id the id of the rattachementLigne to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rattachementLigne, or with status 404 (Not Found)
     */
    @GetMapping("/rattachement-lignes/{id}")
    public ResponseEntity<RattachementLigne> getRattachementLigne(@PathVariable Long id) {
        log.debug("REST request to get RattachementLigne : {}", id);
        Optional<RattachementLigne> rattachementLigne = rattachementLigneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rattachementLigne);
    }

    /**
     * DELETE  /rattachement-lignes/:id : delete the "id" rattachementLigne.
     *
     * @param id the id of the rattachementLigne to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rattachement-lignes/{id}")
    public ResponseEntity<Void> deleteRattachementLigne(@PathVariable Long id) {
        log.debug("REST request to delete RattachementLigne : {}", id);
        rattachementLigneRepository.deleteById(id);
        rattachementLigneSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rattachement-lignes?query=:query : search for the rattachementLigne corresponding
     * to the query.
     *
     * @param query the query of the rattachementLigne search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rattachement-lignes")
    public ResponseEntity<List<RattachementLigne>> searchRattachementLignes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RattachementLignes for query {}", query);
        Page<RattachementLigne> page = rattachementLigneSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rattachement-lignes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }



    @PostMapping("/rattachement-lignes/register")
    public ResponseEntity<RattachementLigne> addRattachementLigne(
        @Valid @RequestBody RattachementLigneVM ligneVM)  throws URISyntaxException{

        ligneVM.setNumero(FormatNumberPhoneUtil.getNumberFormat(ligneVM.getNumero()));

        ligneVM.setLogin(FormatNumberPhoneUtil.getNumberFormat(ligneVM.getLogin()));
        RattachementLigne rattachement = new RattachementLigne();
        if (ligneVM.getNumero().matches(Constants.LOGIN_REGEX_VALID_NUMBER)) {

            checkNumberIfUsed(ligneVM.getNumero(), ligneVM.getLogin());

            Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(ligneVM.getLogin());

            if(!accountB2C.isPresent()){
                throw new LigneNotFoundException();
            }

            rattachement.setNumero(ligneVM.getNumero());
            rattachement.setTypeNumero(ligneVM.getTypeNumero());
            rattachement.setCodeVerification(ligneVM.getCodeVerification());
            rattachement.setTypeVerification(ligneVM.getTypeVerification());
            rattachement.setStatut(ligneVM.getStatut());
            rattachement.setAccountB2C(accountB2C.get());

            try {
                rattachement = rattachementLigneRepository.save(rattachement);
                }
                catch (Exception e) {
                        log.debug("Error when creating rattache ligne : {}", ligneVM);
                    }

            }

        return ResponseEntity.created(new URI("/api/rattachement-lignes/" + rattachement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, rattachement.getId().toString()))
            .body(rattachement);
    }


    @GetMapping("/rattachement-lignes/get-all-number/{msisdn}")
    @Timed
    public ResponseEntity<List<InfoNumberVM>> getRattachementLignes(
        @PathVariable String msisdn) {
        log.debug("REST request to get RattachementLigne : {}", msisdn);

        List<InfoNumberVM> infoNumberVMList = new ArrayList<>();

        msisdn = FormatNumberPhoneUtil.getNumberFormat(msisdn);

        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero(msisdn);

        if (user.isPresent()) {
            List<RattachementLigne> list =  rattachementLigneRepository.findAllByAccountB2C(user.get());

            for (RattachementLigne rattachementLigne : list) {

                InfoNumberVM infoNumberVMS = new InfoNumberVM();

                infoNumberVMS.setMsisdn(rattachementLigne.getNumero());

                SouscriptionDto dto = getSouscription(rattachementLigne.getNumero());
                if(dto != null){

                    infoNumberVMS.setProfil(dto.getProfil());
                    infoNumberVMS.setFormule(dto.getNomOffre());

                }else {
                    infoNumberVMS.setProfil("");
                    infoNumberVMS.setFormule("");

                }

                infoNumberVMList.add(infoNumberVMS);
            }
        }
        return ResponseEntity.ok(infoNumberVMList);
    }



    @PostMapping("/rattachement-lignes/delete-multiple")
    @Timed
    public ResponseEntity<RattachementLignesDeleteMultipleVM> deleteMultipleRattachementLigne(
        @Valid @RequestBody RattachementLignesDeleteMultipleVM deleteListe) {

        for (String numero : deleteListe.getListMsisdn()) {
            Optional<RattachementLigne> ligne = rattachementLigneRepository
                .findByNumero(numero);
            if (ligne.isPresent()) {
                rattachementLigneRepository.delete(ligne.get());
                deleteListe.setDeleted(true);
            }
        }
        return ResponseEntity.ok(deleteListe);

    }

    public void checkNumberIfUsed(String number, String login){

        number = FormatNumberPhoneUtil.getNumberFormat(number);
        Optional<RattachementLigne> ligne = rattachementLigneRepository
            .findByNumero(number);

        if(ligne.isPresent()){
            throw new LigneAlreadyRattachedException();
        }

        Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(login);

        if(!account.isPresent()){
            throw new LigneNotFoundException();
        }

        Optional<AccountB2C> rattachementLigne = accountB2CRepository.findOneByNumero(number);
        if(rattachementLigne.isPresent()){
            throw new LoginAlreadyUsedException();
        }

    }


    public SouscriptionDto getSouscription(String msisdn){

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(msisdn));
        try {

            ResponseEntity<SouscriptionDto> response = ServiceSelfcareb2cSOAP.getSouscription(restTemplate, request);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            else if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }

        }catch (Exception e){

            log.debug("Exception get souscription abonne : {}", msisdn);
        }

        return null;
    }
}
