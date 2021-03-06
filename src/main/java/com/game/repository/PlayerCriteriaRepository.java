package com.game.repository;

import com.game.entity.Player;
import com.game.searchfilteringsorting.PlayerPage;
import com.game.searchfilteringsorting.PlayerSearchCriteria;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PlayerCriteriaRepository {

    @Qualifier(value = "entityManager")
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public PlayerCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Player> findAllWithFilters(PlayerPage playerPage, PlayerSearchCriteria playerSearchCriteria){
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        Predicate predicate = getPredicate(playerSearchCriteria,playerRoot);
        criteriaQuery.where(predicate);
        setOrder(playerPage, criteriaQuery, playerRoot);

        TypedQuery<Player> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(playerPage.getPageNumber() * playerPage.getPageSize());
        typedQuery.setMaxResults(playerPage.getPageSize());

        Pageable pageable = getPageable(playerPage);

        long playerCount = getPlayerCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(),pageable,playerCount);
    }

    private Predicate getPredicate(PlayerSearchCriteria playerSearchCriteria, Root<Player> playerRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(playerSearchCriteria.getName())){
            predicates.add(criteriaBuilder.like(playerRoot.get("name"),"%" + playerSearchCriteria.getName() + "%"));
        }
        if(Objects.nonNull(playerSearchCriteria.getTitle())){
            predicates.add(criteriaBuilder.like(playerRoot.get("title"),"%" + playerSearchCriteria.getTitle() + "%"));
        }
        if(Objects.nonNull(playerSearchCriteria.getRace())){
            predicates.add(criteriaBuilder.equal(playerRoot.get("race"),playerSearchCriteria.getRace()));
        }
        if(Objects.nonNull(playerSearchCriteria.getProfession())){
            predicates.add(criteriaBuilder.equal(playerRoot.get("profession"),playerSearchCriteria.getProfession()));
        }
        if(Objects.nonNull(playerSearchCriteria.getAfter())){
            Date after = new Date(playerSearchCriteria.getAfter());
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(playerRoot.get("birthday"),after));
        }
        if(Objects.nonNull(playerSearchCriteria.getBefore())){
            Date before = new Date(playerSearchCriteria.getBefore());
            predicates.add(criteriaBuilder.lessThanOrEqualTo(playerRoot.get("birthday"),before));
        }
        if(Objects.nonNull(playerSearchCriteria.isBanned())){
            predicates.add(criteriaBuilder.equal(playerRoot.get("banned"),playerSearchCriteria.isBanned()));
        }
        if(Objects.nonNull(playerSearchCriteria.getMinExperience())){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(playerRoot.get("experience"),playerSearchCriteria.getMinExperience()));
        }
        if(Objects.nonNull(playerSearchCriteria.getMaxExperience())){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(playerRoot.get("experience"),playerSearchCriteria.getMaxExperience()));
        }
        if(Objects.nonNull(playerSearchCriteria.getMinLevel())){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(playerRoot.get("level"),playerSearchCriteria.getMinLevel()));
        }
        if(Objects.nonNull(playerSearchCriteria.getMaxLevel())){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(playerRoot.get("level"),playerSearchCriteria.getMaxLevel()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(PlayerPage playerPage, CriteriaQuery<Player> criteriaQuery, Root<Player> playerRoot) {
        if(playerPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(playerRoot.get(playerPage.getSortBy())));
        }else criteriaQuery.orderBy(criteriaBuilder.desc(playerRoot.get(playerPage.getSortBy())));
    }

    private Pageable getPageable(PlayerPage playerPage) {
        Sort sort = Sort.by(playerPage.getSortDirection(), playerPage.getSortBy());
        return PageRequest.of(playerPage.getPageNumber(), playerPage.getPageSize(),sort);
    }

    private long getPlayerCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(long.class);
        Root<Player> countRoot = countQuery.from(Player.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
