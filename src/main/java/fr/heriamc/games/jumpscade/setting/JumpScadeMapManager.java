package fr.heriamc.games.jumpscade.setting;

import fr.heriamc.api.game.GameState;
import fr.heriamc.games.engine.map.GameMapManager;
import fr.heriamc.games.engine.map.slime.SlimeGameMap;
import fr.heriamc.games.engine.map.slime.SlimeMap;
import fr.heriamc.games.engine.map.slime.SlimeWorldLoader;
import fr.heriamc.games.engine.team.GameTeamColor;
import fr.heriamc.games.engine.utils.json.ConfigLoader;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.region.JumpScadeWinRegion;

public class JumpScadeMapManager extends GameMapManager<JumpScadeGame, SlimeMap, SlimeWorldLoader> {

    private final SlimeMap waitingMap, arenaMap;

    public JumpScadeMapManager(JumpScadeGame game) {
        super(game, new SlimeWorldLoader());
        this.waitingMap = new SlimeGameMap(getFormattedTypeMapName("waiting"), getFormattedMapTemplateName("waitingroom"));
        this.arenaMap = new SlimeGameMap(getFormattedTypeMapName("arena"), getFormattedMapTemplateName("classic"));
    }

    @Override
    public void setup() {
        var configuration = ConfigLoader.loadConfig("JumpScade", "classic", JumpScadeConfiguration.class);

        addMap(waitingMap);
        addMap(arenaMap);

        getMapLoader().load(waitingMap).whenComplete((slimeMap, throwable) -> {
            slimeMap.setSpawn(configuration.getSpawn().setWorld(slimeMap));
            slimeMap.getWorld().setGameRuleValue("doFireTick", "false");

            game.getWaitingRoom().setMap(slimeMap);
        });

        getMapLoader().load(arenaMap).whenComplete((slimeMap, throwable) -> {
            var pos1 = configuration.getWinRegionPos1();
            var pos2 = configuration.getWinRegionPos2();

            pos1.setWorld(slimeMap.getWorld());
            pos2.setWorld(slimeMap.getWorld());

            game.setWinRegion(new JumpScadeWinRegion(game, configuration.getWinRegionPos1(), configuration.getWinRegionPos2()));
            game.getTeam(GameTeamColor.RED).ifPresent(team -> team.setSpawnPoint(configuration.getRed().setWorld(slimeMap)));
            game.getTeam(GameTeamColor.BLUE).ifPresent(team -> team.setSpawnPoint(configuration.getBlue().setWorld(slimeMap)));

            slimeMap.getWorld().setGameRuleValue("doFireTick", "false");

            game.setState(GameState.WAIT);
        });
    }

    @Override
    public void end() {
        delete(waitingMap);
        delete(arenaMap);
    }

}