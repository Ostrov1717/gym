package org.example.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GenericDAO<T> {

        private final Storage storage;
        private final Class<T> entityClass;
        private static final Logger logger = LoggerFactory.getLogger(GenericDAO.class);

        public GenericDAO(Storage storage, Class<T> entityClass) {
            this.storage = storage;
            this.entityClass = entityClass;
            logger.info("GenericDAO<{}> bean has been created",entityClass.getSimpleName());
        }

        public T save(T entity, Long id) {
            return storage.save(entityClass, id, entity);
        }

        public T findById(Long id) {
            return storage.findById(entityClass, id);
        }

        public void delete(Long id) {
            storage.delete(entityClass, id);
        }


        public Map<Long, T> getAll() {
            return storage.getAll(entityClass);
        }
    }
