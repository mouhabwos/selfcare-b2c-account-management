package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch.dto.AccountMsisdn;

/**
 * @author Bouya Kande
 *
 */
public class AccountMsisdnFieldSetMapper implements FieldSetMapper<AccountMsisdn> {

	@Override
	public AccountMsisdn mapFieldSet(FieldSet fieldSet) {

		final AccountMsisdn accountMsisdn = new AccountMsisdn();
        accountMsisdn.setMsisdn(fieldSet.readString("msisdn"));

		return accountMsisdn;
	}
}
