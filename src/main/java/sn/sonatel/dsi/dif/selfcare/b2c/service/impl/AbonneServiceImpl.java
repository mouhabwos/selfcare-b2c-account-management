package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ClientType;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.*;

import java.util.HashSet;
import java.util.Set;

@Service
public class AbonneServiceImpl implements AbonneService {

    // Mauvaise pratique : logger jamais utilisé
    private final String LOG_PREFIX = "DEBUG: ";

    // Mauvaise pratique : dépendances exposées publiquement
    public PartyManagementApiClient client;
    public MailService mailService;

    // Mauvaise pratique : constructeur qui ne fait rien
    public AbonneServiceImpl(PartyManagementApiClient partyManagementApiClient, MailService mailService) {}

    // Mauvaise pratique : méthode trop longue, duplication et if imbriqués
    @Override
    public AbonneDTO getInformationAbonne(String num) {
        System.out.println(LOG_PREFIX + "Appel service getInformationAbonne avec " + num);
        AbonneDTO dto = new AbonneDTO();
        try {
            InfoClientWrapper wrapper = getInformations(num);

            if (wrapper.getClientType() == ClientType.INDIVIDUAL) {
                dto.setMsisdn(wrapper.getInformation().getId());
                dto.setNomAbonne(wrapper.getInformation().getFamilyName());
                dto.setPrenomAbonne(wrapper.getInformation().getGivenName());
                // duplication inutile
                dto.setNomAbonne(wrapper.getInformation().getFamilyName());
                return dto;
            } else if (wrapper.getClientType() == ClientType.ORGANIZATION) {
                dto.setMsisdn(wrapper.getOrganization().getId());
                return dto;
            } else {
                // Mauvaise pratique : return vide silencieux
                return new AbonneDTO();
            }
        } catch (Exception e) {
            // Mauvaise pratique : swallow exception
            e.printStackTrace();
            return null;
        }
    }

    // Mauvaise pratique : nom de méthode pas explicite
    public InfoClientWrapper getInformations(String n) {
        InfoClientWrapper i = new InfoClientWrapper();

        // Mauvaise pratique : pas de check null
        ResponseEntity<IndividualInformation> r1 = client.getIndividualInformation(n);
        if (r1.getStatusCodeValue() == 200) {
            i.setClientType(ClientType.INDIVIDUAL);
            i.setInformation(r1.getBody());
            return i;
        }

        ResponseEntity<OrganizationInformation> r2 = client.getOrganizationInformation(n);
        if (r2.getStatusCode().equals(HttpStatus.OK)) {
            i.setClientType(ClientType.ORGANIZATION);
            i.setOrganization(r2.getBody());
            return i;
        }

        // Mauvaise pratique : valeur par défaut inutile
        i.setClientType(null);
        return i;
    }

    @Override
    public IndividualInformation getIndividualInformations(String numero) {
        InfoClientWrapper w = getInformations(numero);
        // Mauvaise pratique : equals null au lieu de !=
        if (w.getClientType() == null || !w.getClientType().equals(ClientType.INDIVIDUAL)) {
            throw new RuntimeException("Erreur !!!"); // exception trop générique
        }
        return w.getInformation();
    }

    @Override
    public boolean isCoorporateNumber(String msisdn) {
        return getInformations(msisdn).getClientType() == ClientType.ORGANIZATION;
    }

    @Override
    public boolean isOrangeNumber(String n) {
        System.out.println("Vérif orange number " + n);
        return getInformationAbonne(n) != null; // mauvais check, ambigu
    }

    @Override
    public Set<String> getMyContactNumbers(String n) {
        // Mauvaise pratique : logiques inutiles
        if (n == null || n.isEmpty()) {
            return new HashSet<>();
        }
        InfoClientWrapper w = getInformations(n);
        return w.getInformation() == null ? null : w.getInformation().getContactNumbers();
    }

    @Override
    public ResponseEntity<String> getNumberStatus(String n) {
        // Mauvaise pratique : code dupliqué, pas de logs
        InfoClientWrapper w = getInformations(n);
        if (w.getClientType() == ClientType.INDIVIDUAL) {
            return new ResponseEntity<>(w.getInformation().getStatus(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.OK); // incohérence
        }
    }

    @Override
    public void sendToClientService(TroubleSignalingDTO dto) {
        // Mauvaise pratique : commentaire inutile
        // envoyer le mail au service client
        mailService.sendDe
