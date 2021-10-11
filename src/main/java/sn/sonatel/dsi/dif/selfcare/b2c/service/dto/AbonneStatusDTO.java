package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import lombok.Builder;
import lombok.Data;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.AccountStatus;

@Data
@Builder
public class AbonneStatusDTO {
   private AccountStatus accountStatus;
}
