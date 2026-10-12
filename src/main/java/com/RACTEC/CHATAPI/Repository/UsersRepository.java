package com.RACTEC.CHATAPI.Repository;

import com.RACTEC.CHATAPI.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u JOIN u.chatRooms c WHERE c.id = :roomId")
    List<Users> getAllUsersInRoom(@Param("roomId") Long roomId);

    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}
