package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch.dto.AccountMsisdn;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;

import java.util.Optional;


@StepScope
@Component
public class AccountItemProcessor implements ItemProcessor<AccountMsisdn, AccountB2C> {

    private static final Logger log = LoggerFactory.getLogger(AccountItemProcessor.class);

    private final AccountB2CRepository accountB2CRepository;

    public AccountItemProcessor(AccountB2CRepository accountB2CRepository) {
        this.accountB2CRepository = accountB2CRepository;
    }


    @Override
    public AccountB2C process(AccountMsisdn accountMsisdn){
        log.debug("Service processor to map the read response to a AccountB2C object: {}", accountMsisdn.getMsisdn());
        String msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(accountMsisdn.getMsisdn());
        if(!msisdn.equals("")){
            Optional<AccountB2C> accountB2COptional = accountB2CRepository.findOneByNumero(msisdn);
            if(accountB2COptional.isPresent()){
                return accountB2COptional.get();
            }else {
                log.debug("Msisdn {} for Account not found", msisdn);
            }

        }else {
            log.warn("Msisdn is not valid : {}", msisdn);
        }
        return new AccountB2C();
    }
}
