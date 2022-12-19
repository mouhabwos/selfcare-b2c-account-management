package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.SponsorMessageErrors.MSISDN_ALREADY_USED;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponsorService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UploadResponse;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.MsisdnAlreadyUsedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.SponsorException;

/**
 *
 * @author sogui
 * @since 1.4.0
 */

@RunWith(SpringRunner.class)
class SponsorServiceImplTest {

    @Mock
    private SponsorRepository sponsorRepository;

    private SponsorService sponsorService;

    private static final String TEST_MSISDN = "776713165";
    private static final String TEST_MSISDN2 = "775167761";

    static List<Sponsor> listSponsors;

    static {
        listSponsors = new ArrayList<>();

        Sponsor sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setMsisdn(TEST_MSISDN);
        sponsor.setFirstName("Sogui");
        sponsor.setLastName("Konaté");
        listSponsors.add(sponsor);

        Sponsor sponsor2 = new Sponsor();
        sponsor2.setId(2L);
        sponsor2.setMsisdn(TEST_MSISDN2);
        sponsor2.setFirstName("Pape");
        sponsor2.setLastName("Diop");
        listSponsors.add(sponsor2);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        sponsorService = new SponsorServiceImpl(sponsorRepository);
    }

    @Test
    void testAlreadyPresentSaveNumber() throws SponsorException {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setMsisdn(TEST_MSISDN);
        Mockito.when(sponsorRepository.getSponsorByMsisdn(sponsor.getMsisdn())).thenReturn(Optional.of(listSponsors.get(0)));

        SponsorException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            SponsorException.class,
            () -> {
                sponsorService.save(sponsor);
            },
            "SponsorException was expected"
        );

        org.junit.jupiter.api.Assertions.assertEquals(MSISDN_ALREADY_USED, thrown.getMessage());
    }

    @Test
    void testUpdateNumberPresent() {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(2L);
        sponsor.setMsisdn(TEST_MSISDN);
        Mockito.when(sponsorRepository.getSponsorByMsisdn(sponsor.getMsisdn())).thenReturn(Optional.of(listSponsors.get(0)));
        Mockito.when(sponsorRepository.findById(sponsor.getId())).thenReturn(Optional.of(listSponsors.get(1)));

        MsisdnAlreadyUsedException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            MsisdnAlreadyUsedException.class,
            () -> {
                sponsorService.update(sponsor);
            },
            "MsisdnAlreadyUsedException was expected"
        );

        org.junit.jupiter.api.Assertions.assertEquals("Ce numéro est deja  utilisé", thrown.getTitle());
    }

    @Test
    void testUploadDuplicate() throws Exception {
        Path path = Paths.get("duplicate.xls");
        String name = "file";
        String originalFileName = "duplicate.xls";
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);

        //Mockito.when(sponsorRepository.findAll()).thenReturn(listSponsors);
        Mockito.when(sponsorRepository.getSponsorByMsisdn(TEST_MSISDN)).thenReturn(Optional.of(listSponsors.get(0)));

        UploadResponse response = sponsorService.upload(file);

        assertThat(response.getNbreSponsorAdded()).isEqualTo(3);
        assertThat(response.getErrorList()).hasSize(2);
        assertThat(response.getErrorList().get(0).getMsisdn()).isEqualTo(TEST_MSISDN);
    }
}
