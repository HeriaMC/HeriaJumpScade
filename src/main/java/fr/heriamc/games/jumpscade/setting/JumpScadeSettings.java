package fr.heriamc.games.jumpscade.setting;

import fr.heriamc.bukkit.game.size.GameSize;
import fr.heriamc.games.engine.GameSettings;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.board.GameBoard;
import fr.heriamc.games.engine.board.GameBoardManager;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.board.JumpScadeBoard;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;

public class JumpScadeSettings extends GameSettings<JumpScadeMapManager> {

    public JumpScadeSettings(GameSize gameSize) {
        super(gameSize, new GameBoardManager());
    }

    @Override
    public GameBoard<?, ?> defaultBoard(MiniGame game, BaseGamePlayer gamePlayer) {
        return new JumpScadeBoard((JumpScadeGame) game, (JumpScadePlayer) gamePlayer);
    }

}