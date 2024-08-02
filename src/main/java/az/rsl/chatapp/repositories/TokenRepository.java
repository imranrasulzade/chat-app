package az.rsl.chatapp.repositories;

import az.rsl.chatapp.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token  t inner join User u
            on t.user.id = u.id
            where u.id = :userId and (t.expired = false)
            """)
    List<Token> findAllValidTokenByUser(Long userId);
}
