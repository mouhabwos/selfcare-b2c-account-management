package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.DowloadManager;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.FileNullException;

@Service
public class DowloadManagerImpl implements DowloadManager {

    private final ServiceFile serviceFile;


    public DowloadManagerImpl(ServiceFile serviceFile) {
        this.serviceFile = serviceFile;

    }



    @Override
    public UserInfoOuvertureCompte addResources(UserInfoOuvertureCompte user)  {

        Resource body = serviceFile.downloadFile(user.getFormulaire()).getBody();


        user.setObjectFormulaire(body);
        user.setObjectRectoID(serviceFile.downloadFile(user.getRectoID()).getBody());
        if(user.getVersoID() != null){
            user.setObjectVersoID(serviceFile.downloadFile(user.getVersoID()).getBody());
        }

        if(user.getObjectFormulaire() != null && user.getObjectRectoID() != null){

            return user;
        }else {
            throw new FileNullException();
        }


    }

}
