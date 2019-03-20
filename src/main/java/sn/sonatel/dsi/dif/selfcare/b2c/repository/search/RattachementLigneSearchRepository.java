package sn.sonatel.dsi.dif.selfcare.b2c.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;

/**
 * Spring Data Elasticsearch repository for the RattachementLigne entity.
 */
public interface RattachementLigneSearchRepository extends ElasticsearchRepository<RattachementLigne, Long> {
}
