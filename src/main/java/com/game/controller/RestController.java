package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.searchfilteringsorting.PlayerPage;
import com.game.searchfilteringsorting.PlayerSearchCriteria;
import com.game.service.PlayerService;
import com.game.service.impl.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;


@Controller
@RequestMapping(value = "/rest/players")
public class RestController {

    @Autowired
    PlayerService ps = new PlayerServiceImpl();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Player> getPlayer(@PathVariable long id) {
        if (id < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player reqPlayer = ps.getPlayer(id);
        if (reqPlayer != null) {
            return new ResponseEntity<>(reqPlayer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player4Create) {
        if (player4Create == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player4Create.getBirthday() == null || player4Create.getExperience() == null || player4Create.getName() == null ||
                player4Create.getProfession() == null || player4Create.getRace() == null || player4Create.getTitle() == null ||
                player4Create.getExperience() < 0 || player4Create.getExperience() > 10000000 || player4Create.getName().length() > 12 ||
                player4Create.getTitle().length() > 30) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(player4Create.getBirthday());
        if (cal.get(Calendar.YEAR) < 2000 || cal.get(Calendar.YEAR) > 3000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ps.addPlayer(player4Create);
        return new ResponseEntity<Player>(player4Create, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<HttpStatus> deletePlayer(@PathVariable long id) {
        if (id < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player4Delete = ps.getPlayer(id);
        if (player4Delete == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ps.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> updatePlayer(@PathVariable long id, @RequestBody Player info4Update) {
        if (id < 1) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        if (info4Update == null) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        Player player4Update = ps.getPlayer(id);
        if (player4Update == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (info4Update.getName() != null) {

            if (info4Update.getName().length() > 12) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            player4Update.setName(info4Update.getName());
        }
        if (info4Update.getTitle() != null) {
            if (info4Update.getTitle().length() > 30) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            player4Update.setTitle(info4Update.getTitle());
        }

        if (info4Update.getRace() != null) player4Update.setRace(info4Update.getRace());
        if (info4Update.getProfession() != null) player4Update.setProfession(info4Update.getProfession());
        if (info4Update.getBirthday() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(info4Update.getBirthday());
            if (cal.get(Calendar.YEAR) < 2000 || cal.get(Calendar.YEAR) > 3000) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            player4Update.setBirthday(info4Update.getBirthday());
        }
        if (info4Update.getExperience() != null) {
            if (info4Update.getExperience() > 0 && info4Update.getExperience() <= 10000000) {
                player4Update.setExperience(info4Update.getExperience());
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        player4Update.setBanned(info4Update.isBanned());
        ps.updatePlayer(player4Update);
        return new ResponseEntity<>(player4Update, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Player>> getPlayers (@RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "title", required = false) String title,
                                                    @RequestParam(value = "race", required = false) Race race,
                                                    @RequestParam(value = "profession", required = false) Profession profession,
                                                    @RequestParam(value = "after", required = false) Long after,
                                                    @RequestParam(value = "before", required = false) Long before,
                                                    @RequestParam(value = "banned", required = false) Boolean banned,
                                                    @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                    @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                    @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                    @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                    @RequestParam(value = "order", required = false) PlayerOrder order,
                                                    @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        PlayerPage pp = new PlayerPage();
        if (order != null) {
        pp.setSortBy(order.getFieldName());
        }
        if(pageNumber != null)pp.setPageNumber(pageNumber);
        if(pageSize != null)pp.setPageSize(pageSize);
        PlayerSearchCriteria psc = new PlayerSearchCriteria(name,title,race,profession,after,before,banned,minExperience,
                maxExperience,minLevel,maxLevel);
        return new ResponseEntity<List<Player>>(ps.sfs(pp, psc).getContent(), HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Long> getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "title", required = false) String title,
                                                @RequestParam(value = "race", required = false) Race race,
                                                @RequestParam(value = "profession", required = false) Profession profession,
                                                @RequestParam(value = "after", required = false) Long after,
                                                @RequestParam(value = "before", required = false) Long before,
                                                @RequestParam(value = "banned", required = false) Boolean banned,
                                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        PlayerPage pp = new PlayerPage();
        PlayerSearchCriteria psc = new PlayerSearchCriteria(name,title,race,profession,after,before,banned,minExperience,
                maxExperience,minLevel,maxLevel);
        return new ResponseEntity<>(ps.sfs(pp, psc).getTotalElements(), HttpStatus.OK);
    }

}
