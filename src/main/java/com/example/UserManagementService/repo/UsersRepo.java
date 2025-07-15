package com.example.UserManagementService.repo;


import com.example.UserManagementService.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<UserMaster,Integer> {
}
