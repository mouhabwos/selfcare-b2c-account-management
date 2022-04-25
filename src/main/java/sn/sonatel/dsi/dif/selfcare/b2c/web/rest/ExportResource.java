package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.SearchFilterItem;
import sn.sonatel.dsi.dif.selfcare.b2c.service.FileInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.export.ExportService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.RedocMessages;
import tech.jhipster.web.util.PaginationUtil;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportResource {

    private final Logger log = LoggerFactory.getLogger ( ExportResource.class );

    private final ExportService exportService;
    private final FileInformationService fileInformationService;
    public ExportResource(ExportService exportService, FileInformationService fileInformationService) {
        this.exportService = exportService;
        this.fileInformationService = fileInformationService;
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
    public ResponseEntity uploadFileMsisdn(@RequestPart(name = "file") MultipartFile file, Principal principal){
        log.debug ( "REST request to upload file of list of Msisdn ");
        exportService.uploadFileMsisdn(file, principal.getName());
        return ResponseEntity.accepted().build();
    }

    @ApiOperation(value = Message.ExportUsers.UPLOADED_FILE_INFORMATION,
        notes = RedocMessages.ExportUsers.DESCRIPTION_UPLOADED_FILE_INFORMATION,
        response = ResponseEntity.class)
    @Auditable(description = Message.ExportUsers.UPLOADED_FILE_INFORMATION)
    @GetMapping("/v1/uploaded-file-information")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<FileInformation>> getListInformationFileUploaded(@RequestParam SearchFilterItem searchFilterItem, Pageable pageable, @RequestParam(required = false) ZonedDateTime startDate, @RequestParam(required = false) ZonedDateTime endDate, @RequestParam(required = false) String user){
        log.debug("Rest request to get information of file uploaded : {}, {}, {}, {}", searchFilterItem, startDate, endDate, user);
        Page<FileInformation> page = fileInformationService.getInformationFileUploaded(searchFilterItem, pageable, startDate, endDate, user);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


}
