package sn.sonatel.dsi.dif.selfcare.b2c.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RattachementLigneSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RattachementLigneSearchRepositoryMockConfiguration {

    @MockBean
    private RattachementLigneSearchRepository mockRattachementLigneSearchRepository;

}
