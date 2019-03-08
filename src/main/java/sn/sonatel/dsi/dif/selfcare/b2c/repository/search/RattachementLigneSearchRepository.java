package sn.sonatel.dsi.dif.selfcare.b2c.repository.search;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RattachementLigne entity.
 */
public interface RattachementLigneSearchRepository extends ElasticsearchRepository<RattachementLigne, Long> {
}
