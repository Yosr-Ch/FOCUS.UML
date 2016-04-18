package com.ensi.pcd.repository.search;

import com.ensi.pcd.domain.Diagram;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Diagram entity.
 */
public interface DiagramSearchRepository extends ElasticsearchRepository<Diagram, Long> {
}
