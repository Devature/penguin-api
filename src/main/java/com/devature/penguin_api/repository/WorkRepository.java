package com.devature.penguin_api.repository;

import com.devature.penguin_api.model.Work;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource()
@CrossOrigin
public interface WorkRepository extends
                                    PagingAndSortingRepository<Work, Long>,
                                    CrudRepository<Work, Long> {
    
}
