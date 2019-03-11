package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.search.AccountB2CSearchRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall.ServiceSelfcareUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;
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
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AccountB2C.
 */
@RestController
@RequestMapping("/api/account-management")
public class AccountB2CResource {

    private final Logger log = LoggerFactory.getLogger(AccountB2CResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementAccountB2C";

    private final AccountB2CRepository accountB2CRepository;

    private final AccountB2CSearchRepository accountB2CSearchRepository;

    private final RattachementLigneRepository rattachementLigneRepository;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    public AccountB2CResource(AccountB2CRepository accountB2CRepository, AccountB2CSearchRepository accountB2CSearchRepository, RattachementLigneRepository rattachementLigneRepository) {
        this.accountB2CRepository = accountB2CRepository;
        this.accountB2CSearchRepository = accountB2CSearchRepository;
        this.rattachementLigneRepository = rattachementLigneRepository;
    }

    /**
     * POST  /account-b-2-cs : Create a new accountB2C.
     *
     * @param accountB2C the accountB2C to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountB2C, or with status 400 (Bad Request) if the accountB2C has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-b-2-cs")
    public ResponseEntity<AccountB2C> createAccountB2C(@Valid @RequestBody AccountB2C accountB2C) throws URISyntaxException {
        log.debug("REST request to save AccountB2C : {}", accountB2C);
        if (accountB2C.getId() != null) {
            throw new BadRequestAlertException("A new accountB2C cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountB2C result = accountB2CRepository.save(accountB2C);
        accountB2CSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/account-b-2-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /account-b-2-cs/register : Create a new accountB2C.
     *
     * @param managedUserVM the accountB2C to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountB2C, or with status 400 (Bad Request) if the accountB2C has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/register")
    public ResponseEntity<AccountB2C> registerAccountB2C(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(managedUserVM.getLogin());
        Optional<RattachementLigne> ligne = rattachementLigneRepository.findByNumero(managedUserVM.getLogin());

        if(accountB2C.isPresent()){
            throw new LoginAlreadyUsedException();
        }

        if(ligne.isPresent()){
            throw new LigneAlreadyRattachedException();

        }

        AccountB2C result =  new AccountB2C();
        try{

            managedUserVM.setActivated(true);
            HttpEntity<ManagedUserVM> request = new HttpEntity<>(managedUserVM);
            ServiceSelfcareUAA.regiserAccount(restTemplate, request);
            AccountB2C account =  new AccountB2C();
            account.setNumero(managedUserVM.getLogin());
            account.setEmail(managedUserVM.getEmail());
            account.setFirstName(managedUserVM.getFirstName());
            account.setLastName(managedUserVM.getLastName());
            account.setImagePrfil(managedUserVM.getImageprofil());
            result = accountB2CRepository.save(account);
    }catch (Exception e){

        throw new BadRequestAlertException("Utilisateur non enregistré", ENTITY_NAME, "");

    }


        return ResponseEntity.created(new URI("/api/account-b-2-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-b-2-cs : Updates an existing accountB2C.
     *
     * @param accountB2C the accountB2C to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountB2C,
     * or with status 400 (Bad Request) if the accountB2C is not valid,
     * or with status 500 (Internal Server Error) if the accountB2C couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-b-2-cs")
    public ResponseEntity<AccountB2C> updateAccountB2C(@Valid @RequestBody AccountB2C accountB2C) throws URISyntaxException {
        log.debug("REST request to update AccountB2C : {}", accountB2C);
        if (accountB2C.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountB2C result = accountB2CRepository.save(accountB2C);
        accountB2CSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountB2C.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-b-2-cs : get all the accountB2CS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountB2CS in body
     */
    @GetMapping("/account-b-2-cs")
    public ResponseEntity<List<AccountB2C>> getAllAccountB2CS(Pageable pageable) {
        log.debug("REST request to get a page of AccountB2CS");
        Page<AccountB2C> page = accountB2CRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-b-2-cs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /account-b-2-cs/:id : get the "id" accountB2C.
     *
     * @param id the id of the accountB2C to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountB2C, or with status 404 (Not Found)
     */
    @GetMapping("/account-b-2-cs/{id}")
    public ResponseEntity<AccountB2C> getAccountB2C(@PathVariable Long id) {
        log.debug("REST request to get AccountB2C : {}", id);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountB2C);
    }

    /**
     * DELETE  /account-b-2-cs/:id : delete the "id" accountB2C.
     *
     * @param id the id of the accountB2C to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-b-2-cs/{id}")
    public ResponseEntity<Void> deleteAccountB2C(@PathVariable Long id) {
        log.debug("REST request to delete AccountB2C : {}", id);
        accountB2CRepository.deleteById(id);
        accountB2CSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/account-b-2-cs?query=:query : search for the accountB2C corresponding
     * to the query.
     *
     * @param query the query of the accountB2C search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/account-b-2-cs")
    public ResponseEntity<List<AccountB2C>> searchAccountB2CS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountB2CS for query {}", query);
        Page<AccountB2C> page = accountB2CSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/account-b-2-cs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/check_number/{msisdn}")
    public ResponseEntity checkNumber(@PathVariable String msisdn) {
        msisdn = FormatNumberPhoneUtil.getNumberFormat(msisdn);

        Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(msisdn);
        if(account.isPresent()){
            throw new LoginAlreadyUsedException();
        }

        Optional<RattachementLigne> ligne = rattachementLigneRepository.findByNumero(msisdn);
        if(ligne.isPresent()){
            throw new LigneAlreadyRattachedException();
        }
        return ResponseEntity.ok().build();
    }

}
