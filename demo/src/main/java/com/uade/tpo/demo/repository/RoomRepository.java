package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Room;
import com.uade.tpo.demo.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByUser(User user);
    
    @Query("SELECT r FROM Room r WHERE r.user.userId = :userId")
    List<Room> findByUserId(@Param("userId") Long userId);
    
    Optional<Room> findByUserAndName(User user, String name);
    
    @Query("SELECT r FROM Room r WHERE r.user.userId = :userId AND r.name = :name")
    Optional<Room> findByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);
}

