//***************************************************************************************
//
//     Filename: UserRepository.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file provides functionality for user search.
//
//***************************************************************************************

package com.kmccol1.to_do_app.Data;

import com.kmccol1.to_do_app.Models.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

//***************************************************************************************

public interface UserRepository extends CrudRepository<User, Integer>
{
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

//***************************************************************************************
