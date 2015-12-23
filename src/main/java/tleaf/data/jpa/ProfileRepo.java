package tleaf.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import tleaf.Profile;

import java.util.List;

@org.springframework.context.annotation.Profile("orm-jpa")
public interface ProfileRepo extends JpaRepository<Profile, Long>, ProfileSweeper {
    Profile findByUsername(String username);
    List<Profile> findByUsernameLike(String username);
    //List<Profile> findByUsernameOrFullnameLike(String username, String fullname);
}
