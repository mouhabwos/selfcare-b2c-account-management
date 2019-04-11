package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

public class FileNullException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public FileNullException() {
        super ( "Les fichies ne sont pas récupérés", "fileManagement", "fileManagement" );
    }
}
