package com.ensi.pcd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ensi.pcd.domain.Diagram;
import com.ensi.pcd.repository.DiagramRepository;
import com.ensi.pcd.repository.search.DiagramSearchRepository;
import com.ensi.pcd.web.rest.util.HeaderUtil;
import com.ensi.pcd.web.rest.util.PaginationUtil;
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
     * PUT  /diagrams : Updates an existing diagram.
     *
     * @param diagram the diagram to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diagram,
     * or with status 400 (Bad Request) if the diagram is not valid,
     * or with status 500 (Internal Server Error) if the diagram couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/diagrams",
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
    }

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

    @RequestMapping(value = "/projects/{id}/diagrams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Diagram>> getDiagramsByProjectId(@PathVariable Long id, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get Diagrams of a project : {}", id);
        Page<Diagram> page = diagramRepository.findByProjectId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects/"+id.toString()+"/diagrams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /diagrams/:id : get the "id" diagram.
     *
     * @param id the id of the diagram to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diagram, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/diagrams/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagram> getDiagram(@PathVariable Long id) {
        log.debug("REST request to get Diagram : {}", id);
        Diagram diagram = diagramRepository.findOne(id);
        return Optional.ofNullable(diagram)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diagrams/:id : delete the "id" diagram.
     *
     * @param id the id of the diagram to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/diagrams/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiagram(@PathVariable Long id) {
        log.debug("REST request to delete Diagram : {}", id);
        diagramRepository.delete(id);
        diagramSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diagram", id.toString())).build();
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

}
