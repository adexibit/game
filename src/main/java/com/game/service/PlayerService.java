package com.game.service;


import com.game.entity.Player;
import com.game.searchfilteringsorting.PlayerPage;
import com.game.searchfilteringsorting.PlayerSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlayerService {

    Player addPlayer(Player player);
    Player getPlayer(long id);
    Player updatePlayer(Player player);
    Boolean deletePlayer(long id);
    List<Player> getAll();
    Page<Player> sfs(PlayerPage playerPage, PlayerSearchCriteria playerSearchCriteria);
}
