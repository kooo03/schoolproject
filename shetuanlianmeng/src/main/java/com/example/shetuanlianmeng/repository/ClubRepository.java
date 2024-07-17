package com.example.shetuanlianmeng.repository;

import com.example.shetuanlianmeng.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByNameContaining(String name);
    List<Club> findByCategory(String category);
    boolean existsByName(String name);
    Optional<Club> findByUserId(Long userId); // 添加这个方法
    List<Club> findByNameIn(List<String> names);
}
