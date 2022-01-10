package ru.tal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tal.entity.Book;
import ru.tal.entity.Profile;
import ru.tal.entity.ResultGame;
import ru.tal.entity.Words;
import ru.tal.model.TopCount;
import ru.tal.service.MainService;

import java.util.List;

import static ru.tal.service.GameType.ENGLISH;
import static ru.tal.service.GameType.MATESHA;
import static ru.tal.service.GameType.SNAKE;

@Controller
public class ProfileController {

    private final MainService mainService;

    @Autowired
    public ProfileController(MainService profileService) {
        this.mainService = profileService;
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Profile profile, Model model) {
        Profile getProfile = mainService.getProfileById(profile.getId());
        model.addAttribute("maths", mainService.resultGame(MATESHA, getProfile.getId()));
        model.addAttribute("snake", mainService.resultGame(SNAKE, getProfile.getId()));
        model.addAttribute("english", mainService.resultGame(ENGLISH, getProfile.getId()));
        model.addAttribute("countBook", mainService.getCountBookByIdProfile(getProfile.getId()));
        model.addAttribute("profile", getProfile);
        model.addAttribute("title", "Main");
        return "main";
    }

    @GetMapping("/matesha")
    public String matesha(@AuthenticationPrincipal Profile profile, Model model, ResultGame resultGame) {
        model.addAttribute("maths", resultGame);
        model.addAttribute("title", "Matesha");
        return "matesha";
    }

    @PostMapping("/save-maths")
    public String saveMaths(@AuthenticationPrincipal Profile profile, ResultGame resultGame) {
        mainService.saveResultGame(profile, resultGame, MATESHA);
        return "redirect:/main";
    }

    @GetMapping("/best-maths")
    public String bestMaths(@AuthenticationPrincipal Profile profile, Model model) {
        Profile getProfile = mainService.getProfileById(profile.getId());
        List<TopCount> topSource = mainService.getTopSourceByType(MATESHA);
        model.addAttribute("title", "Record");
        model.addAttribute("profile", getProfile);
        model.addAttribute("top", topSource);
        return "top-count";
    }

    @GetMapping("/snake")
    public String snake(@AuthenticationPrincipal Profile profile, Model model, ResultGame resultGame) {
        model.addAttribute("snake", resultGame);
        model.addAttribute("title", "Snake");
        return "snake";
    }

    @PostMapping("/save-snake")
    public String saveSnake(@AuthenticationPrincipal Profile profile, ResultGame resultGame) {
        mainService.saveResultGame(profile, resultGame, SNAKE);
        return "redirect:/main";
    }

    @GetMapping("/best-snake")
    public String bestSnake(@AuthenticationPrincipal Profile profile, Model model) {
        Profile getProfile = mainService.getProfileById(profile.getId());
        List<TopCount> topSource = mainService.getTopSourceByType(SNAKE);
        model.addAttribute("title", "Record");
        model.addAttribute("profile", getProfile);
        model.addAttribute("top", topSource);
        return "top-count";
    }

    @GetMapping("/english")
    public String english(@AuthenticationPrincipal Profile profile, Model model, ResultGame resultGame) {
        List<Words> words = mainService.getAll();
        model.addAttribute("title", "English");
        model.addAttribute("english", resultGame);
        model.addAttribute("words", words);
        return "english";
    }

    @PostMapping("/save-english")
    public String saveEnglish(@AuthenticationPrincipal Profile profile, ResultGame resultGame) {
        mainService.saveResultGame(profile, resultGame, ENGLISH);
        return "redirect:/main";
    }

    @GetMapping("/best-english")
    public String bestEnglish(@AuthenticationPrincipal Profile profile, Model model) {
        Profile getProfile = mainService.getProfileById(profile.getId());
        List<TopCount> topCount = mainService.getTopSourceByType(ENGLISH);
        model.addAttribute("title", "Record");
        model.addAttribute("profile", getProfile);
        model.addAttribute("top", topCount);
        return "top-count";
    }

    @GetMapping("/book")
    public String book(@AuthenticationPrincipal Profile profile, Model model) {
        Profile getProfile = mainService.getProfileById(profile.getId());
        List<Book> books = mainService.getAllBookByIdProfile(getProfile.getId());
        model.addAttribute("title", "Record");
        model.addAttribute("profile", getProfile);
        model.addAttribute("books", books);
        model.addAttribute("isGame", mainService.getCountBookByIdProfile(getProfile.getId()) > 4);
        return "book";
    }

    @GetMapping("/save-book")
    public String saveWord(@AuthenticationPrincipal Profile profile, @RequestParam String param) {
        mainService.insetListBook(param, profile);
        return "redirect:/book";
    }

    @GetMapping("english-book")
    public String englishWithMyBook(@AuthenticationPrincipal Profile profile, Model model, ResultGame resultGame) {
        List<Book> books = mainService.getAllBookByIdProfile(profile.getId());
        model.addAttribute("words", books);
        model.addAttribute("title", "English");
        model.addAttribute("english", resultGame);
        return "english";
    }

//    @SneakyThrows
//    @GetMapping("/parse")
//    public String parse() {
//        try {
//            List<Words> wordsList = new ArrayList<>();
//            Document doc = Jsoup.connect("https://studynow.ru/dicta/allwords").get();
//            Element element = doc.getElementById("wordlist");
//            for (int i = 0; i < 5000; i++) {
//                Words word = new Words();
//                Node w = element.child(0).child(i).child(1).childNode(0);
//                word.setWord(w.toString());
//                Node t = element.child(0).child(i).child(2).childNode(0);
//                word.setTranslate(t.toString());
//                wordsList.add(word);
//            }
//            mainService.insertListWord(wordsList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/main";
//    }

}
