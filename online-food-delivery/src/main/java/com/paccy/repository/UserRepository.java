package com.paccy.repository;

import com.paccy.model.USER_ROLE;
import com.paccy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
 User findByUsername(String username);

 public    User findByEmail(String username);
 List<User> findByUsernameContainingOrEmailContaining(String username, String email);





  long countByStatus(String status);

  // Count users by role
  long countByRole(USER_ROLE role);



  // Additional search methods
  List<User> findByUsernameContainingIgnoreCase(String username);

  List<User> findByEmailContainingIgnoreCase(String email);

  List<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(String username, String email);


}
