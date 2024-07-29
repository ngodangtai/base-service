package com.company.module.base.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.module.base.view.ApplicationView;

@Repository
public interface ApplicationViewRepository extends CrudRepository<ApplicationView, Long> {

}
