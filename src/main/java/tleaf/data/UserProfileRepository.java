package tleaf.data;

import tleaf.UserProfile;

public interface UserProfileRepository {

    UserProfile save(UserProfile userProfile);

    UserProfile findByUsername(String username);

}
