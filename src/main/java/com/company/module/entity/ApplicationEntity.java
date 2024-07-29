package com.company.module.entity;

import com.company.module.base.service.Description;
import com.company.module.common.TableNameConstants;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = TableNameConstants.TABLE_APPLICATION)
public class ApplicationEntity extends BaseEntity {
    @Id
    @SequenceGenerator(name = "application_id_generator", sequenceName = "application_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_generator")
    @Column(name = "APPID")
    private Long appId;

    @Column(name = "APPNAME")
    @Description(value = "app name", required = true)
    private String appName;

    @Column(name = "APPDESC")
    private String appDesc;
}
