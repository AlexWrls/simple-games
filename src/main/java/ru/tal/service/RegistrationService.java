package ru.tal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tal.entity.Profile;
import ru.tal.entity.Role;
import ru.tal.repository.ProfileRepo;

import java.util.Collections;

@Service
@Slf4j
public class RegistrationService implements UserDetailsService {

    private final ProfileRepo profileRepo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationService(ProfileRepo profileRepo, PasswordEncoder passwordEncoder) {
        this.profileRepo = profileRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Profile profile = profileRepo.getByEmail(email);
        if (profile == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return profile;
    }

    public boolean addProfile(Profile profile) {
        String email = profile.getEmail();
        Profile accountFormDb = profileRepo.getByEmail(email);

        if (accountFormDb != null) {
            log.warn("Профиль по этим email уже существует");
            return false;
        }
        profile.setRole(Collections.singleton(Role.USER));

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        profileRepo.save(profile);
        log.warn("Профиль успешно создан");
        return true;
    }
}
