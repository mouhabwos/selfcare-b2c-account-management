package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.export.ExportService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.RedocMessages;

import java.security.Principal;

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
    @PostMapping("/all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity exportAllUsers(Principal principal){
        log.debug ( "REST request to export All User in to an excel file with email : {}", principal.getName() );
        exportService.exportAllUsers(principal.getName());
        return ResponseEntity.accepted().build();

    }

    @ApiOperation(value = Message.ExportUsers.IMPORT_FILE_MSISDN,
        notes = RedocMessages.ExportUsers.DESCRIPTION_IMPORT_FILE_MSISDN,
        response = ResponseEntity.class)
    @Auditable(description = Message.ExportUsers.IMPORT_FILE_MSISDN)
    @PostMapping("/v1/file-campaign-flow")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity uploadFileMsisdn(@RequestPart(name = "file") MultipartFile file){
        log.debug ( "REST request to upload file of list of Msisdn ");
        exportService.uploadFileMsisdn(file);
        return ResponseEntity.accepted().build();
    }


}
