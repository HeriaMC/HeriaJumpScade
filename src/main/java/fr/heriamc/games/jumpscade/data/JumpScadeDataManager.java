package fr.heriamc.games.jumpscade.data;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.data.PersistentDataManager;
import fr.heriamc.games.jumpscade.player.JumpScadePlayerData;

import java.util.UUID;

public class JumpScadeDataManager extends PersistentDataManager<UUID, JumpScadePlayerData> {

    public JumpScadeDataManager(HeriaAPI heriaApi) {
        super(heriaApi.getRedisConnection(), heriaApi.getMongoConnection(), "jumpscade", "jumpscade");
    }

    @Override
    public JumpScadePlayerData getDefault() {
        return new JumpScadePlayerData(null, 0, 0);
    }

}