package com.game.entity;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "player")
public class Player {
    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUntilNextLevel(int untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id", length = 20, nullable = false )
    private Long id;

    @Column(name = "name", length = 12)
    private String name;

    @Column(name = "title", length = 30)
    private String title;

    @Column(name = "race")
    @Enumerated(EnumType.STRING)
    private Race race;

    @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "level")
    private int level;

    @Column(name = "untilNextLevel")
    private int untilNextLevel;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "banned")
    private boolean banned;
}
