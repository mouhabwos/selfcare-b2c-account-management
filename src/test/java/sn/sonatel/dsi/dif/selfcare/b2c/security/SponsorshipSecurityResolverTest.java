package sn.sonatel.dsi.dif.selfcare.b2c.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;

class SponsorshipSecurityResolverTest {

    @Mock
    private SponsorRepository mockSponsorRepository;

    private SponsorshipSecurityResolver sponsorshipSecurityResolverUnderTest;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        sponsorshipSecurityResolverUnderTest = new SponsorshipSecurityResolver(mockSponsorRepository);
    }

    @Test
    void testIsSponsor() {
        // Setup
        final String msisdn = "770000000";
        List<Sponsor> list = new ArrayList<>();
        Sponsor sponsor = new Sponsor();
        sponsor.setMsisdn(msisdn);
        sponsor.setMatricule("jdk");
        list.add(sponsor);
        when(mockSponsorRepository.findAll()).thenReturn(list);

        // Run the test
        final boolean result = sponsorshipSecurityResolverUnderTest.isSponsor(msisdn);

        // Verify the results
        Assertions.assertTrue(result);
    }

    @Test
    void testIsSponsorEmptyList() {
        // Setup
        final String msisdn = "770000000";
        List<Sponsor> list = new ArrayList<>();

        when(mockSponsorRepository.findAll()).thenReturn(list);

        // Run the test
        final boolean result = sponsorshipSecurityResolverUnderTest.isSponsor(msisdn);

        // Verify the results
        Assertions.assertFalse(result);
    }
}
