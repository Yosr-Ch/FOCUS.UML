package com.ensi.pcd.repository;

import com.ensi.pcd.domain.Diagram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Diagram entity.
 */
public interface DiagramRepository extends JpaRepository<Diagram,Long> {


    Page<Diagram> findByProjectId(Long prjId, Pageable pageable);
}
