package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailSendService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 * class MailSendServiceImpl
 */

@Service
class MailSendServiceImpl implements MailSendService {


    private final MailSendRepository mailSendRepository;

    public MailSendServiceImpl(MailSendRepository mailSendRepository) {
        this.mailSendRepository = mailSendRepository;
    }

    @Override
    public String getStatusMailSend(String idRequest) {
        Optional<Mail> request = mailSendRepository.findByIdRequest(idRequest);

        if(request.isPresent()){
            return request.get().getStatus()+"";
        }else throw new BadRequestAlertException("idRequest non Trouve",idRequest,null);

    }
}
