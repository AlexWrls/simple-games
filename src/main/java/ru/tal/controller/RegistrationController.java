package ru.tal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tal.entity.Profile;
import ru.tal.service.RegistrationService;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal Profile profile, Model model) {
        model.addAttribute("profile", profile);
        return "login";
    }

    @GetMapping("/registration")
    public String registration(
            Profile profile,
            Model model) {
        model.addAttribute("profile", profile);
        model.addAttribute("title", "Registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("passwordConfirm") String passwordConfirm,
            Model model,
            @Valid Profile profile,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return registration(profile, model);
        }

        if (passwordConfirm.isEmpty()) {
            model.addAttribute("password2Error", "Поле неможет быть пустым");
            return "registration";
        }

        if (profile.getPassword() != null && !profile.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают!");
            return "registration";
        }
        boolean addProfile = registrationService.addProfile(profile);
        if (!addProfile) {
            model.addAttribute("message", "Пользователь с данным именем уже зарегистрирован!");
            return "registration";
        }
        model.addAttribute("message", "Профиль успешно создан");
        return "login";
    }
}
