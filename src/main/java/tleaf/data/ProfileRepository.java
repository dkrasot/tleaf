package tleaf.data;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import tleaf.Profile;

import java.util.List;

public interface ProfileRepository {

    long count();

    @CachePut(value = "ehCache", key = "#result.id")
    Profile save(Profile profile);

    @Cacheable("ehCache")
    Profile findOne(long id);

    @Cacheable("ehCache")
    Profile findByUsername(String username);

    @Cacheable("ehCache")
    List<Profile> findAll();

}
