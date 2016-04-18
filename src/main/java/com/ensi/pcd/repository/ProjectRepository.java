package com.ensi.pcd.repository;

import com.ensi.pcd.domain.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("select project from Project project where project.user.login = ?#{principal.username}")
    List<Project> findByUserIsCurrentUser();

    @Query("select project from Project project where project.user.login = ?#{principal.username}")
    Page<Project> findAllForCurrentUser(Pageable pageable);

}
