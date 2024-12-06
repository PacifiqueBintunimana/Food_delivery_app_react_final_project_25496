package com.paccy.service;

import com.paccy.model.User;

public interface UserService {
   // public User registerUser(UserDTO userDTO) throws Exception;

    public User userByJwtToken(String jwt) throws Exception;

    public User finduserByEmail(String email) throws Exception;
    boolean doesEmailExist(String email);

}
