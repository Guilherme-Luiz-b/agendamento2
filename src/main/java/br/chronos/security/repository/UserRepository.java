package br.chronos.security.repository;

import br.chronos.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT                      " +
            "           u.id,                   " +
            "           u.name,                 " +
            "           u.username,             " +
            "           u.email,                " +
            "           u.password,             " +
            "           r.name,                 " +
            "           COALESCE(u.ativo, true) " +
            "       FROM                        " +
            "           User u                  " +
            "           JOIN u.roles r          " +
            "       WHERE                       " +
            "           u.id = :id              ")
    List<Object[]> findUserPrincipal(@Param("id") Long id);

}