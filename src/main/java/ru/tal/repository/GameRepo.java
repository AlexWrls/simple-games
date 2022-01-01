package ru.tal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tal.entity.ResultGame;

@Repository
public interface GameRepo extends JpaRepository<ResultGame,Long> {
}
