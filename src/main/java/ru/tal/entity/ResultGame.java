package ru.tal.entity;

import lombok.Data;
import ru.tal.service.GameType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "result_game")
public class ResultGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_profile")
    private Long idProfile;

    @Column(name = "count")
    private Integer count;

    @Column(name = "type_game")
    @Enumerated(EnumType.STRING)
    private GameType gameType;

}
