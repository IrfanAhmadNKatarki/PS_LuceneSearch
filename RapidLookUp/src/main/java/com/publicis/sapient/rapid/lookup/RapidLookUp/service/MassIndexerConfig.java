package com.publicis.sapient.rapid.lookup.RapidLookUp.service;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MassIndexerConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void rebuildIndex() {
        try {
            SearchSession searchSession = Search.session(entityManager);
            searchSession.massIndexer()
                    .threadsToLoadObjects(4)  // Number of threads
                    .batchSizeToLoadObjects(100)  // Batch size
                    .startAndWait();
            System.out.println("Mass indexing completed successfully!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Indexing interrupted", e);
        }
    }
}
