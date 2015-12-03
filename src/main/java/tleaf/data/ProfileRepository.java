package tleaf.data;

import tleaf.Profile;

import java.util.List;

public interface ProfileRepository {

    Profile save(Profile profile);

    Profile findByUsername(String username);

    Profile findById(long id);

    List<Profile> findAll();

}
