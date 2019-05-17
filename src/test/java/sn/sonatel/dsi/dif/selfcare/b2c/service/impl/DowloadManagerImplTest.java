package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class DowloadManagerImplTest {

    @Mock
    private ServiceFile mockServiceFile;

    @InjectMocks
    private DowloadManagerImpl dowloadManagerImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        //dowloadManagerImplUnderTest = new DowloadManagerImpl(mockServiceFile);
    }

    @Test
    public void testAddResources() {

        UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setOperationTitle("Ouverture compte OM");
        user.setFormulaire("1.PNG");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        user.setEmail("bouya@gmail.com");

        //final UserInfoOuvertureCompte expectedResult = null;

        // Run the test
      //  final UserInfoOuvertureCompte result = dowloadManagerImplUnderTest.addResources(user);

        // Verify the results
        //assertEquals(expectedResult, result);
    }
}
