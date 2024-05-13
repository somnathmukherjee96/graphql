package com.dailycode.graphqldemo.service;

import com.dailycode.graphqldemo.model.Player;
import com.dailycode.graphqldemo.model.Team;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlayerService {
    private List<Player> players = new ArrayList<>();
    AtomicInteger id = new AtomicInteger(0);

    public List<Player> findAll(){
        return players;
    }

    public Optional<Player> findOne(Integer id){
        return players.stream().filter(player -> player.id() == id).findFirst();
    }

    public Player create(String name, Team team){
        Player player = new Player(id.incrementAndGet(), name, team);
        players.add(player);
        return player;
    }

    public Player delete(Integer id){
        Player player = players.stream().filter(p -> p.id()==id).findFirst().orElseThrow(IllegalArgumentException::new);
        players.remove(player);
        return player;
    }

    public Player update(Integer id, String name, Team team){
        Player updatedPlayer = new Player(id, name, team);
        Optional<Player> optionalPlayer = players.stream().filter(p -> p.id()==id).findFirst();
        if(optionalPlayer.isPresent()){
            Player player = optionalPlayer.get();
            int index = players.indexOf(player);
            players.set(index, updatedPlayer);
        } else {
            throw new IllegalArgumentException("Invalid Player");
        }
        return updatedPlayer;
    }

    @PostConstruct
    private void init(){
        players.add(new Player(id.incrementAndGet(), "MS Dhoni", Team.CSK));
        players.add(new Player(id.incrementAndGet(), "Virat Kohli", Team.RCB));
        players.add(new Player(id.incrementAndGet(), "Rohit Sharma", Team.MI));
        players.add(new Player(id.incrementAndGet(), "Gautam Gambhir", Team.KKR));
        players.add(new Player(id.incrementAndGet(), "Hardik Pandya", Team.MI));
        players.add(new Player(id.incrementAndGet(), "Rishabh Pant", Team.GT));

    }
}
