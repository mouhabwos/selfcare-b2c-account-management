package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneNotFoundException;

import java.util.Optional;

/**
 * Service class for managing users accountB2C.
 */
@Service
public class AccountB2CServiceImpl implements AccountB2CService {

    private final AccountB2CRepository accountB2CRepository;

    public AccountB2CServiceImpl(AccountB2CRepository accountB2CRepository) {
        this.accountB2CRepository = accountB2CRepository;
    }


    @Override
    public void tutorialView(String msisdn) {

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdn);

        if(accountB2C.isPresent()){
            accountB2C.get().setTutoViewed(true);
            accountB2CRepository.save(accountB2C.get());
        }else {
            throw  new LigneNotFoundException();
        }

    }
}
