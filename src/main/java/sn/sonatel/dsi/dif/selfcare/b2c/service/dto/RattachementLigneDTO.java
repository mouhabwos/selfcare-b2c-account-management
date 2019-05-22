package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class RattachementLigneDTO extends RattachementBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private AccountB2C accountB2C;

    public RattachementLigneDTO() {
        //Default constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountB2C getAccountB2C() {
        return accountB2C;
    }

    public void setAccountB2C(AccountB2C accountB2C) {
        this.accountB2C = accountB2C;
    }
}
