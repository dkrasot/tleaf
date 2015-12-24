package tleaf.data;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import tleaf.entity.Profile;

import java.util.List;

public interface ProfileRepository {

    long count();

    @CachePut(value = "myCache", key = "#result.id") // maybe #result.username?? unique username/email
    Profile save(Profile profile);

    @Cacheable(value = "myCache", key = "#result.id")
    Profile findOne(long id);

    @Cacheable(value = "myCache", key = "#result.username")
    Profile findByUsername(String username);

    List<Profile> findAll();

}
