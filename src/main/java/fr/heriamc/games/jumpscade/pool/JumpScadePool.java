package fr.heriamc.games.jumpscade.pool;

import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.jumpscade.JumpScadeGame;

import java.util.UUID;
import java.util.function.Supplier;

public class JumpScadePool extends GamePool<JumpScadeGame> {

    public JumpScadePool() {
        super(JumpScadeGame.class, "JumpScade Pool", HeriaServerType.JUMPSCADE, 1, 6, DirectConnectStrategy.FILL_GAME);
    }

    @Override
    public Supplier<JumpScadeGame> newGame() {
        return JumpScadeGame::new;
    }

    @Override
    public Supplier<JumpScadeGame> newGame(UUID uuid, Object... objects) {
        return () -> new JumpScadeGame(uuid);
    }

}