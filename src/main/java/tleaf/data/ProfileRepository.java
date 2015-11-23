package tleaf.data;

import tleaf.Profile;

public interface ProfileRepository {

    Profile save(Profile profile);

    Profile findByUsername(String username);

}
