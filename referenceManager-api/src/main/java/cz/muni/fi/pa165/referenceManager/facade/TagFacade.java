package cz.muni.fi.pa165.referenceManager.facade;

import cz.muni.fi.pa165.referenceManager.dto.TagCreateDTO;
import cz.muni.fi.pa165.referenceManager.dto.TagDTO;
import cz.muni.fi.pa165.referenceManager.dto.TagUpdateDTO;

import java.util.List;

/**
 * Interface representing the TagFacade.
 *
 * @author Lenka Smitalova
 */
public interface TagFacade {

    Long createTag(TagCreateDTO tagCreateDTO);

    Long createTag(TagDTO tagDTO);

    void updateTagName(TagDTO tagDTO, String newName);

    void removeTag(Long tagId);

    TagDTO findById(Long id);

    List<TagDTO> findAllTags();

    void addReference(Long tagId, Long referenceId);

    void removeReference(Long tagId, Long referenceId);

}
