package co.edu.cue.pruebasConActions.repositories;

import co.edu.cue.pruebasConActions.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByUserEmailEndingWith(String domain);

}
