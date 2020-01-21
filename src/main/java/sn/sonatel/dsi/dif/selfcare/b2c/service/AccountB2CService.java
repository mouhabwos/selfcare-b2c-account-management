package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.CheckNumberRequest;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;


import java.util.Optional;

public interface AccountB2CService {

    void updateTutorialView(String msisdn);

    AccountB2C createAccountB2C(AccountB2CDTO accountB2C);

    AccountB2C registerAccountB2C(ManagedUserVM managedUserVM);

    AccountB2C updateAccountB2C(AccountB2CDTO accountB2C);

    Page<AccountB2C> getAllAccountB2C(Pageable pageable);

    Optional<AccountB2C> getAccountB2C(Long id);

    boolean emailExistingVerify(String email);

    AccountB2C getAccount(String login);

    void checkNumberV2(CheckNumberRequest checkNumberRequest);

    AccountB2C registerAccountB2CV2(ManagedUserVM managedUserVM);
}
