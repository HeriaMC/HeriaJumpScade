package fr.heriamc.games.jumpscade.setting;

import fr.heriamc.games.engine.point.SinglePoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class JumpScadeConfiguration {

    private String templateName;

    private SinglePoint spawn, red, blue;

    private Location winRegionPos1, winRegionPos2;

}