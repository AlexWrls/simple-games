package ru.tal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tal.entity.Profile;
import ru.tal.entity.ResultGame;
import ru.tal.entity.Words;
import ru.tal.model.TopCount;
import ru.tal.model.TopCountJdbcTemplate;
import ru.tal.repository.GameRepo;
import ru.tal.repository.ProfileRepo;
import ru.tal.repository.ResultGameRepo;
import ru.tal.repository.WordsRepo;

import java.util.List;

@Service
@Slf4j
public class MainService {
    private final ProfileRepo profileRepo;
    private final TopCountJdbcTemplate topCountRepo;
    private final ResultGameRepo gameRepo;
    private final WordsRepo wordsRepo;
    private final GameRepo gameRepoJpa;

    @Autowired
    public MainService(ProfileRepo profileRepo, TopCountJdbcTemplate mathsJdbcTemplate, ResultGameRepo gameRepo, WordsRepo wordsRepo, GameRepo gameRepoJpa) {
        this.profileRepo = profileRepo;
        this.topCountRepo = mathsJdbcTemplate;
        this.gameRepo = gameRepo;
        this.wordsRepo = wordsRepo;
        this.gameRepoJpa = gameRepoJpa;
    }

    public Profile getProfileById(long id) {
        try {
            return profileRepo.getById(id);
        } catch (Exception e) {
            log.error("Ошибка получения профиля по Id: " + e.getMessage());
            throw new RuntimeException("Ошибка получения профиля по Id: " + e.getMessage());
        }
    }

    public List<Words> getAll() {
        return wordsRepo.getRandomWords();
    }

    public void saveResultGame(Profile profile, ResultGame resultGame, GameType gameType) {
        try {
            ResultGame profileResult = resultGame(gameType, profile.getId());
            if (profileResult == null) {
                profileResult = new ResultGame();
                profileResult.setIdProfile(profile.getId());
                profileResult.setCount(resultGame.getCount());
            } else {
                int record = resultGame.getCount() > profileResult.getCount() ? resultGame.getCount() : profileResult.getCount();
                profileResult.setCount(record);
            }
            profileResult.setGameType(gameType);

            profile.setCredit(profile.getCredit() + resultGame.getCount());
            gameRepoJpa.save(profileResult);
            profileRepo.save(profile);
        } catch (Exception e) {
            log.error("Ошибка сохранения результатов : " + e.getMessage());
            throw new RuntimeException("Ошибка сохранения результатов змейки: " + e.getMessage());
        }
    }

    public List<TopCount> getTopSourceByType(GameType gameType) {
        return topCountRepo.getTopCountByGameType(gameType);
    }

    public ResultGame resultGame(GameType gameType, long idProfile) {
        return gameRepo.getByGameTypeAndIdProfile(gameType, idProfile);
    }

    public void insertListWord(List<Words> wordsList) {
        wordsRepo.insertListWords(wordsList);
    }
}