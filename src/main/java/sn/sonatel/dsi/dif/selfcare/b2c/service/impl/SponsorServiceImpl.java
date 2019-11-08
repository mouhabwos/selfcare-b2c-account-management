package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.SponsorUploadResponse;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponsorService;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UploadResponse;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.MsisdnAlreadyUsedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.SponsorException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.SponsorMessageErrors;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Sponsor}.
 */
@Service
@Transactional
public class SponsorServiceImpl implements SponsorService {

    private final Logger log = LoggerFactory.getLogger(SponsorServiceImpl.class);

    private final SponsorRepository sponsorRepository;

    public SponsorServiceImpl(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    /**
     * Save a sponsor.
     *
     * @param sponsor the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Sponsor save(Sponsor sponsor) throws SponsorException {
        log.debug("Request to save Sponsor : {}", sponsor);
        if(isNumberPresent(sponsor.getMsisdn())){
            throw new SponsorException(SponsorMessageErrors.MSISDN_ALREADY_USED);
        }
        return sponsorRepository.save(sponsor);
    }

    @Override
    public Sponsor update(Sponsor sponsor) {
        log.debug("Request to update Sponsor : {}", sponsor);
        Optional<Sponsor> sponsorToUpdate = findOne(sponsor.getId());
        if(sponsorToUpdate.isPresent() && isNumberPresent(sponsor.getMsisdn()) && getSponsorByMsisdn(sponsor.getMsisdn()).getId().compareTo(sponsorToUpdate.get().getId())!=0){
            throw new MsisdnAlreadyUsedException();
        }
        return sponsorRepository.save(sponsor);
    }

    /**
     * Get all the sponsors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sponsor> findAll(Pageable pageable) {
        log.debug("Request to get all Sponsors");
        return sponsorRepository.findAll(pageable);
    }


    /**
     * Get one sponsor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Sponsor> findOne(Long id) {
        log.debug("Request to get Sponsor : {}", id);
        return sponsorRepository.findById(id);
    }

    @Override
    public Boolean isNumberPresent(String msisdn) {

        Optional<Sponsor> sponsor=sponsorRepository.getSponsorByMsisdn(msisdn);
        return sponsor.isPresent();

    }

    @Override
    public Sponsor getSponsorByMsisdn(String msisdn) {

        Optional<Sponsor> sponsor = sponsorRepository.getSponsorByMsisdn(msisdn);

        return sponsor.orElse(null);
    }

    /**
     * Delete the sponsor by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sponsor : {}", id);
        sponsorRepository.deleteById(id);
    }

    @Override
    public UploadResponse upload(MultipartFile multipartFile) throws SponsorException {
        List<SponsorUploadResponse> errorsList = new ArrayList<>();
        File file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
            multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new SponsorException(SponsorMessageErrors.FILE_ERROR);
        }
        Iterator<Row> rowIterator;

        // Creating a Workbook from an Excel file (.xls or .xlsx)
        try (Workbook workbook = WorkbookFactory.create(file)) {
            // Getting the Sheet at index zero
            Sheet sheet = workbook.getSheetAt(0);
            rowIterator = sheet.rowIterator();

        } catch (IOException e) {
            throw new SponsorException(SponsorMessageErrors.FILE_ERROR);
        } catch (InvalidFormatException e) {
            throw new SponsorException(SponsorMessageErrors.FORMAT_INVALID);
        }

        return getUploadResponse(errorsList, rowIterator);
    }

    @Async
    private UploadResponse getUploadResponse(List<SponsorUploadResponse> sponsorUploadErrorResponseList, Iterator<Row> rowIterator) {
        int sponsorCounter = 0;
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        //Skip first row
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Sponsor sponsor = new Sponsor();
            sponsor.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(dataFormatter.formatCellValue(row.getCell(0))));
            sponsor.setMatricule(dataFormatter.formatCellValue(row.getCell(1)));
            sponsor.setFirstName(dataFormatter.formatCellValue(row.getCell(2)));
            sponsor.setLastName(dataFormatter.formatCellValue(row.getCell(3)));

            try {
                save(sponsor);
                sponsorCounter++;
            } catch (SponsorException ex) {
                SponsorUploadResponse sponsorUploadResponse = new SponsorUploadResponse();
                sponsorUploadResponse.setMsisdn(sponsor.getMsisdn());
                sponsorUploadResponse.setMatricule(sponsor.getMatricule());
                sponsorUploadResponse.setFirstName(sponsor.getFirstName());
                sponsorUploadResponse.setLastName(sponsor.getLastName());
                sponsorUploadResponse.setErrorMsg(ex.getMessage());
                sponsorUploadErrorResponseList.add(sponsorUploadResponse);
            }
        }

        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setNbreSponsorAdded(sponsorCounter);
        uploadResponse.setErrorList(sponsorUploadErrorResponseList);
        return uploadResponse;
    }

}
