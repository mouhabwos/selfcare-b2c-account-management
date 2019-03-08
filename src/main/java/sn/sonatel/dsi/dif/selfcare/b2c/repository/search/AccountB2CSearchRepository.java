package sn.sonatel.dsi.dif.selfcare.b2c.repository.search;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;

import java.util.List;

/**
 * Spring Data Elasticsearch repository for the AccountB2C entity.
 */
public interface AccountB2CSearchRepository extends ElasticsearchRepository<AccountB2C, Long> {

}
