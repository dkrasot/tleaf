package tleaf.data;

import tleaf.Profile;

import java.util.List;

public interface ProfileRepository {

    long count();

    Profile save(Profile profile);

    Profile findByUsername(String username);

    Profile findOne(long id);

    List<Profile> findAll();

}
