package com.game.service.impl;

import com.game.repository.PlayerCriteriaRepository;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import com.game.searchfilteringsorting.PlayerPage;
import com.game.searchfilteringsorting.PlayerSearchCriteria;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerCriteriaRepository playerCriteriaRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Page<Player> sfs(PlayerPage playerPage, PlayerSearchCriteria playerSearchCriteria){
        return playerCriteriaRepository.findAllWithFilters(playerPage,playerSearchCriteria);
    }

    @Override
    public Player addPlayer(Player player){
        player.setLevel((int)(Math.sqrt(2500+200*player.getExperience())-50)/100);
        player.setUntilNextLevel(50*(player.getLevel()+1)*(player.getLevel()+2)- player.getExperience());
        Player addedPlayer = playerRepository.saveAndFlush(player);
        return addedPlayer;
    }

    @Override
    public Player getPlayer(long id){
        Optional<Player> optPlayer = playerRepository.findById(id);
        return optPlayer.orElse(null);
    }
    @Override
    public Player updatePlayer(Player player){
        player.setLevel((int)(Math.sqrt(2500+200*player.getExperience())-50)/100);
        player.setUntilNextLevel(50*(player.getLevel()+1)*(player.getLevel()+2)- player.getExperience());
        playerRepository.saveAndFlush(player);
        return player;
    }

    @Override
    public Boolean deletePlayer(long id){
      Player player4Delete = getPlayer(id);
      if (player4Delete!=null){
          playerRepository.delete(player4Delete);
          return true;
      }
      return false;
    }

    @Override
    public List<Player> getAll(){
        return playerRepository.findAll();
    }



}
