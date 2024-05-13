package com.dailycode.graphqldemo.controller;

import com.dailycode.graphqldemo.model.Player;
import com.dailycode.graphqldemo.model.Team;
import com.dailycode.graphqldemo.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.junit.jupiter.api.Assertions.*;
@Import(PlayerService.class)
@GraphQlTest(PlayerController.class)
class PlayerControllerTest {
    @Autowired GraphQlTester tester;

    @Test
    void testFindAllPlayersShouldReturnAllPlayers(){
        String document = """
                query FindAll {
                    findAll{
                        id
                        name
                        team
                    }
                }
                """;

        tester.document(document)
                .execute()
                .path("findAll")
                .entityList(Player.class)
                .hasSizeGreaterThan(3);
    }

    @Test
    void testValidIdShouldReturnPlayer(){
        String document = """
                query findOne($id: ID) {
                    findOne(id: $id) {
                        id
                        name
                        team
                    }
                }
                """;
        tester.document(document)
                .variable("id", 1)
                .execute()
                .path("findOne")
                .entity(Player.class)
                .satisfies(
                        player -> {
                            Assertions.assertEquals("MS Dhoni", player.name());
                        }
                );
    }

    @Test
    void testInvalidIdShouldReturnNull(){
        String document = """
                query findOne($id: ID) {
                    findOne(id: $id) {
                        id
                        name
                        team
                    }
                }
                """;

        tester.document(document)
                .variable("id", 100)
                .execute()
                .path("findOne")
                .valueIsNull();
    }

    @Test
    void testShouldCreateNewPlayer(){
        String document = """
                mutation create($name: String, $team: Team) {
                    create(name: $name, team: $team){
                        id
                        name
                        team
                    }
                }
                """;
        tester.document(document)
                .variable("name", "Jadeja")
                .variable("team", Team.CSK)
                .execute()
                .path("create")
                .entity(Player.class)
                .satisfies(player -> {
                    Assertions.assertEquals("Jadeja", player.name());
                    Assertions.assertEquals(Team.CSK, player.team());
                });
    }
}