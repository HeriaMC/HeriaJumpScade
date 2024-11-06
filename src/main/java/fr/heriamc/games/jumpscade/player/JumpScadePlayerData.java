package fr.heriamc.games.jumpscade.player;

import com.google.gson.annotations.SerializedName;
import fr.heriamc.api.data.SerializableData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class JumpScadePlayerData implements SerializableData<UUID> {

    @SerializedName("id")
    private UUID identifier;

    // ADD PLAYED GAMES WINS AND LOSES I GUESS
    private int kills, deaths;

    public JumpScadePlayerData updateKills(int kills) {
        this.kills += kills;
        return this;
    }

    public JumpScadePlayerData updateDeaths(int deaths) {
        this.deaths += deaths;
        return this;
    }

}