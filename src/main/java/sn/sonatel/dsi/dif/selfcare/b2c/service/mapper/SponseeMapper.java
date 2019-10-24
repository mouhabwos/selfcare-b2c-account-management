package sn.sonatel.dsi.dif.selfcare.b2c.service.mapper;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.*;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sponsee} and its DTO {@link SponseeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SponseeMapper extends EntityMapper<SponseeDTO, Sponsee> {



    default Sponsee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sponsee sponsee = new Sponsee();
        sponsee.setId(id);
        return sponsee;
    }
}
