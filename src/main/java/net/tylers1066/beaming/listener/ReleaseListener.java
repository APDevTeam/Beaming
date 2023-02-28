package net.tylers1066.beaming.listener;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.events.CraftReleaseEvent;
import net.tylers1066.beaming.Beaming;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReleaseListener implements Listener {
    @EventHandler
    public void onCraftRelease (CraftReleaseEvent event) {
        Map<Player, Craft> crewMap = Beaming.getInstance().getCrewCrafts();
        if (crewMap.containsValue(event.getCraft())) {
            List<Player> removalList = new ArrayList<>();
            for (Player player : crewMap.keySet()) {
                Craft craft = crewMap.get(player);
                if (craft == event.getCraft()) {
                    removalList.add(player);
                }
            }
            for (Player player : removalList) {
                crewMap.remove(player);
            }
        }
    }
}
