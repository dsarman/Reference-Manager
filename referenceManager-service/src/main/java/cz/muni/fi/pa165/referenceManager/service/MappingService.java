package cz.muni.fi.pa165.referenceManager.service;

import org.dozer.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Dozer mapping class
 *
 * @author Jan Bílek
 */

public interface MappingService {

    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

    <T> T mapTo(Object u, Class<T> mapToClass);

    Mapper getMapper();
}
