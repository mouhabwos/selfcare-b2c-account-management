package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.jsmpp.bean.*;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;

@Service
public class SMSNotificationService {

    private final Logger log = LoggerFactory.getLogger(SMSNotificationService.class);

    private final ApplicationProperties applicationProperties;

    public SMSNotificationService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Async
    public void sendSMSPP(String msisdn, String message, String sourceAddress) {
        log.trace("send smspp {} {}", msisdn, message);

        try {
            SMPPSession session = new SMPPSession();

            session.connectAndBind(
                applicationProperties.getSendSms().getServer().getSmsHost(),
                applicationProperties.getSendSms().getServer().getSmsPort(),
                new BindParameter(
                    BindType.BIND_TX,
                    applicationProperties.getSendSms().getServer().getSmsSystemId(),
                    applicationProperties.getSendSms().getServer().getSmsPassword(),
                    applicationProperties.getSendSms().getServer().getSmsSystemType(),
                    TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN,
                    null
                )
            );

            session.submitShortMessage(
                "CMT",
                TypeOfNumber.ALPHANUMERIC,
                NumberingPlanIndicator.UNKNOWN,
                sourceAddress,
                TypeOfNumber.NATIONAL,
                NumberingPlanIndicator.UNKNOWN,
                msisdn,
                new ESMClass(),
                (byte) 0,
                (byte) 1,
                null,
                null,
                new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT),
                (byte) 0,
                new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false),
                (byte) 0,
                message.getBytes()
            );

            session.unbindAndClose();

            log.info("SMS sent successfully!");
        } catch (Exception ex) {
            log.error("Exception during sendSMS: {0} ", ex);
        }
    }
}
