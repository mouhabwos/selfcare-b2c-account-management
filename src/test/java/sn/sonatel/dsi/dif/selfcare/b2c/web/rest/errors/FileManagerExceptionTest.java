package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileManagerExceptionTest {

    private final String defaultMessage = "service non disponible";

    private final String entityName = "Name";

    private final String errorKey = "key";

    private FileManagerException fileManagerException;

    @Before
    public void setUp() throws Exception {
        fileManagerException = new FileManagerException(defaultMessage, entityName, errorKey);
    }

    @Test
    public void testGetAlertParameters(){

        new FileManagerException(defaultMessage, entityName, errorKey);

    }
}
