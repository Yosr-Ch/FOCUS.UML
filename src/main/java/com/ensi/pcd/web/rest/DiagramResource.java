package com.ensi.pcd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ensi.pcd.domain.Diagram;
import com.ensi.pcd.repository.DiagramRepository;
import com.ensi.pcd.repository.ProjectRepository;
import com.ensi.pcd.repository.search.DiagramSearchRepository;
import com.ensi.pcd.web.rest.util.HeaderUtil;
import com.ensi.pcd.web.rest.util.PaginationUtil;
import com.ensi.pcd.web.rest.util.TransformationUtil;
import org.eclipse.uml2.uml.Model;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Diagram.
 */
@RestController
@RequestMapping("/api")
public class DiagramResource {

    private final Logger log = LoggerFactory.getLogger(DiagramResource.class);

    @Inject
    private DiagramRepository diagramRepository;

    @Inject
    private DiagramSearchRepository diagramSearchRepository;

    @Inject
    private ProjectRepository projectRepository;

    /**
     * POST  /diagrams : Create a new diagram.
     *
     * @param diagram the diagram to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diagram, or with status 400 (Bad Request) if the diagram has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/diagrams",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> createDiagram(@Valid @RequestBody Diagram diagram) throws URISyntaxException {
        log.debug("REST request to save Diagram : {}", diagram);
        if (diagram.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("diagram", "idexists", "A new diagram cannot already have an ID")).body(null);
        }
        Diagram result = diagramRepository.save(diagram);
        diagramSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/diagrams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("diagram", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /diagrams : Create a new diagram.
     *
     * @param diagram the diagram to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diagram, or with status 400 (Bad Request) if the diagram has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects/{prjId}/diagrams",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> saveDiagram(@PathVariable Long prjId, @Valid @RequestBody Diagram diagram) throws URISyntaxException {
        log.debug("REST request to save Diagram : {} for project with id", prjId);
        if (diagram.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("diagram", "idexists", "A new diagram cannot already have an ID")).body(null);
        }
        //Diagram result = diagramRepository.save(diagram);
        diagram.setProject(projectRepository.findByUserIsCurrentUserAndId(prjId));
        Diagram result = diagramRepository.save(diagram);
        diagramSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/projects/"+prjId.toString()+"/diagrams" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("diagram", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diagrams : Updates an existing diagram.
     *
     * @param diagram the diagram to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diagram,
     * or with status 400 (Bad Request) if the diagram is not valid,
     * or with status 500 (Internal Server Error) if the diagram couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects/{prjId}/diagrams",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> updateDiagram(@PathVariable Long prjId, @Valid @RequestBody Diagram diagram) throws URISyntaxException {
        log.debug("REST request to update Diagram : {}", diagram);
        if (diagram.getId() == null) {
            return saveDiagram(prjId,diagram);
        }
        //Diagram result = diagramRepository.save(diagram);
        //diagram.setProject(projectRepository.findByUserIsCurrentUserAndId(prjId));
        Diagram result = diagramRepository.save(diagram);
        diagramSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("api/projects/"+prjId.toString()+"/diagrams", diagram.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diagrams : Updates an existing diagram.
     *
     * @param diagram the diagram to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diagram,
     * or with status 400 (Bad Request) if the diagram is not valid,
     * or with status 500 (Internal Server Error) if the diagram couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    /*@RequestMapping(value = "/diagrams",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> updateDiagram(@Valid @RequestBody Diagram diagram) throws URISyntaxException {
        log.debug("REST request to update Diagram : {}", diagram);
        if (diagram.getId() == null) {
            return createDiagram(diagram);
        }
        Diagram result = diagramRepository.save(diagram);
        diagramSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("diagram", diagram.getId().toString()))
            .body(result);
    }*/

    /**
     * GET  /diagrams : get all the diagrams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of diagrams in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/diagrams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Diagram>> getAllDiagrams(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Diagrams");
        Page<Diagram> page = diagramRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/diagrams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{prjId}/diagrams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Diagram>> getDiagramsByProjectId(@PathVariable Long prjId, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get Diagrams of a project : {}", prjId);
        Page<Diagram> page = diagramRepository.findByProjectId(prjId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects/"+prjId.toString()+"/diagrams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /diagrams/:diagId : get the "diagId" diagram.
     *
     * @param diagId the id of the diagram to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diagram, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/diagrams/{diagId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> getDiagram(@PathVariable Long diagId) {
        log.debug("REST request to get Diagram : {}", diagId);
        Diagram diagram = diagramRepository.findOne(diagId);
        return Optional.ofNullable(diagram)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diagrams/:diagId : delete the "diagId" diagram.
     *
     * @param diagId the id of the diagram to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    /*@RequestMapping(value = "/diagrams/{diagId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiagram(@PathVariable Long diagId) {
        log.debug("REST request to delete Diagram : {}", diagId);
        diagramRepository.delete(diagId);
        diagramSearchRepository.delete(diagId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diagram", diagId.toString())).build();
    }*/
    /**
     * DELETE  projects/:prjId/diagrams/:diagId : delete the "diagId" diagram.
     *
     * @param prjId the id of the project
     * @param diagId the id of the diagram to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "projects/{prjId}/diagrams/{diagId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiagram(@PathVariable Long prjId, Long diagId) {
        log.debug("REST request to delete Diagram : {}", diagId);
        diagramRepository.deleteByProjectId(prjId, diagId);
        diagramSearchRepository.delete(diagId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diagram", diagId.toString())).build();
    }

    /**
     * SEARCH  /_search/diagrams?query=:query : search for the diagram corresponding
     * to the query.
     *
     * @param query the query of the diagram search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/diagrams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Diagram>> searchDiagrams(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Diagrams for query {}", query);
        Page<Diagram> page = diagramSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/diagrams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /diagrams : validates an existing diagram.
     *
     * @param diagram the diagram to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diagram,
     * or with status 400 (Bad Request) if the diagram is not valid,
     * or with status 500 (Internal Server Error) if the diagram couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects/{prjId}/diagram",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> validateDiagram(@PathVariable Long prjId, @Valid @RequestBody Diagram diagram) throws URISyntaxException, JDOMException, IOException {
        log.debug("REST request to validate Diagram : {}", diagram);
        if (diagram.getId() == null) {
            return saveDiagram(prjId,diagram);
        }
        Model ucdTest = TransformationUtil.XMLToXMI(diagram.getContent());
        //TransformationUtil.saveUCD(ucdTest);
        String errorsModel = TransformationUtil.XMLToXMIErrors(diagram.getContent());
        String errors = TransformationUtil.validateUCD(ucdTest);

        log.debug("ucdTEsssssssssst", ucdTest);
        if (errors == null && errorsModel == null) {
            diagram.setValidation(true);
            diagram.seterrors_report("valid" + errors + errorsModel);
        Diagram result = diagramRepository.save(diagram);
        diagramSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityValidateAlert("api/projects/"+prjId.toString()+"/diagram", diagram.getId().toString()))
            .body(result);
        }
        else{
            //String errors = TransformationUtil.XMLToXMIErrors(diagram.getContent());
            log.debug("les erreurs te3na !!!!", errors);
            diagram.setValidation(false);
            diagram.seterrors_report("ERROR IN \n" + errors + "\n" + errorsModel + "\n");
            Diagram result = diagramRepository.save(diagram);
            diagramSearchRepository.save(result);
            return ResponseEntity.ok()
                //.headers(HeaderUtil.createEntityValidateAlert("api/projects/"+prjId.toString()+"/diagram", diagram.getId().toString()))
                .body(result);
        /*return ResponseEntity.ok()
            .headers(HeaderUtil.createFailureAlert("api/projects/"+prjId.toString()+"/diagram", diagram.getId().toString(), "Diagram not valid!!"))
            .body(null) ;*/
        }
    }

}
