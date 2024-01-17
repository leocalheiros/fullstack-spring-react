package io.github.leocalheiros.imageliteapi.infra.repository;

import io.github.leocalheiros.imageliteapi.domain.entity.Image;
import io.github.leocalheiros.imageliteapi.domain.enums.ImageExtension;
import io.github.leocalheiros.imageliteapi.infra.repository.specs.GenericSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import java.util.List;

import static io.github.leocalheiros.imageliteapi.infra.repository.specs.ImageSpecs.*;

public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

    default List<Image> findByExtensionAndNameOrTagsLike(ImageExtension extension, String query) {
        // SELECT * FROM IMAGE WHERE 1 = 1
        // ROOT REPRESENTA IMAGEM, Q REPRESENTA A QUERY, E O CRITERIABUILDER REPRESENTA AS ESPECIFICAÇÕES
        Specification<Image> spec = Specification.where(GenericSpecs.conjunction());

        if(extension != null){
            spec = spec.and(extensionEqual(extension));
        }

        if(StringUtils.hasText(query)){
            spec = spec.and(Specification.anyOf(nameLike(query), tagsLike(query)));
        }

        return findAll(spec);
    }
}
