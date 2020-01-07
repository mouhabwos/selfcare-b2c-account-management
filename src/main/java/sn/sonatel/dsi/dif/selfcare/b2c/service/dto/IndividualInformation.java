package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class IndividualInformation {

    private String id;

    private Set<String> contactNumbers = new HashSet<>();

    private String givenName;

    private String familyName;

    private String birthDate;

    private String title;

    private String status;

    private String gender;

    private String maritalStatus;

    private String type;

    private Set<OrganizationIdentification> individualIdentification = new HashSet<>();

    public IndividualInformation() {
        // Default constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(Set<String> contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Set<OrganizationIdentification> getIndividualIdentification() {
        return individualIdentification;
    }

    public void setIndividualIdentification(Set<OrganizationIdentification> individualIdentification) {
        this.individualIdentification = individualIdentification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactNumbers);
    }
}
