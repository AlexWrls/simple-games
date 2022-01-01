package ru.tal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tal.entity.Profile;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    Profile getByEmail(String email);

}
