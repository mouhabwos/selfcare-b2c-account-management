package sn.sonatel.dsi.dif.selfcare.b2c.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "file_information")
public class FileInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "created_by_user")
    private String createdByUser;

    @Column(name = "created_date")
    private ZonedDateTime createdDate  = ZonedDateTime.now();

    @Column(name = "status")
    private String status;
}
