package com.hoaxify.ws.user;




import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByActivationToken(String activationToken);

    Page<User> findByIdNot(long Id, Pageable pageable);
    
    // @Query(value="select u from User u")
    // Page<UserProjection> getAllUsers(Pageable pageable);
}
