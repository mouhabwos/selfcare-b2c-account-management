package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;

import java.util.Date;

public interface FileInformationService {

    FileInformation save(String sourceFile, Date dateCreation);
}
