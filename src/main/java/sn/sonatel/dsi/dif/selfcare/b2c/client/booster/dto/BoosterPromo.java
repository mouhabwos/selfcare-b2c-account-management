package sn.sonatel.dsi.dif.selfcare.b2c.client.booster.dto;

import lombok.Data;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.BoosterTrigger;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class BoosterPromo {

    private Long id;
    private BigDecimal limitTreshold;
    private Instant start;
    private Instant end;
    private Boolean actif;
    private BoosterTrigger boosterTrigger;
    private Set<String> pricePlanIndexes = new HashSet<>();
    private String promoInstanceId;
    private Gift gift;

    @Data
    public static class Gift{
        private Long id;
        private Double value;
        private ValueType valueType;
        private GiftType type;
        private String compteur;
        private String name;
        private String description;
        private Partner partner;

        public enum ValueType {
            PERCENTAGE, AMONT
        }
        public enum GiftType {
            PASS, RECHARGE, COUPON
        }
    }

    @Data
    public static class Partner{
        private Long id;
        private String name;
        private String code;
    }

}
