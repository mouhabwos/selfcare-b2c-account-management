package sn.sonatel.dsi.dif.selfcare.b2c.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SponsorshipSecurityResolverTest {

    @Mock
    private SponsorRepository mockSponsorRepository;

    private SponsorshipSecurityResolver sponsorshipSecurityResolverUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        sponsorshipSecurityResolverUnderTest = new SponsorshipSecurityResolver(mockSponsorRepository);
    }

    @Test
    public void testIsSponsor() {
        // Setup
        final String msisdn = "770000000";
        List<Sponsor> list = new ArrayList<>();
        Sponsor sponsor = new Sponsor();
        sponsor.setMsisdn(msisdn);
        sponsor.setMatricule("jdk");
        list.add(sponsor);
        Sponsor sponsor1 = new Sponsor();
        sponsor1.setMsisdn("778000000");
        sponsor1.setMatricule("jdk");
        when(mockSponsorRepository.findAll()).thenReturn(list);

        // Run the test
        final boolean result = sponsorshipSecurityResolverUnderTest.isSponsor(msisdn);

        // Verify the results
        assertTrue(result);
    }


    @Test
    public void testIsSponsorEmptyList() {
        // Setup
        final String msisdn = "770000000";
        List<Sponsor> list = new ArrayList<>();

        when(mockSponsorRepository.findAll()).thenReturn(list);

        // Run the test
        final boolean result = sponsorshipSecurityResolverUnderTest.isSponsor(msisdn);

        // Verify the results
        assertFalse(result);
    }
}
