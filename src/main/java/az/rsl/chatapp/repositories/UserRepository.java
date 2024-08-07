package az.rsl.chatapp.repositories;

import az.rsl.chatapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    List<User> findUsersByIdIsNotAndStatusTrueAndRole_IdOrderByUserNameAsc(Long id,Long roleId);

    List<User> findUsersByIdIsNotAndStatusTrueAndRole_IdAndUserNameStartingWithIgnoreCaseOrderByUserNameAsc(
            Long id, Long roleId, String userName);


    User findUsersByIdIsNot(Long id);
}
