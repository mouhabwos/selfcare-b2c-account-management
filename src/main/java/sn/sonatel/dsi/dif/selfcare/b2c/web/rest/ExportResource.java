package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.export.ExportService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.RedocMessages;

@RestController
@RequestMapping("/api/export")
public class ExportResource {

    private final Logger log = LoggerFactory.getLogger ( ExportResource.class );

    private final ExportService exportService;

    public ExportResource(ExportService exportService) {
        this.exportService = exportService;
    }


    @ApiOperation(value = Message.ExportUsers.EXPORT_ALL_USER,
        notes = RedocMessages.ExportUsers.DESCRIPTION_EXPORT_ALL_USER,
        response = Boolean.class)
    @Auditable(description = Message.ExportUsers.EXPORT_ALL_USER)
    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity exportAllUsers(){
        log.debug ( "REST request to export All User in to an excel file" );
        exportService.exportAllUsers();
        return ResponseEntity.accepted().build();

    }


}
