package com.retail.profile.repository;

import com.retail.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  User findUserByEmail(String orderId);
  User findUserByPhoneNumber(String orderId);
}