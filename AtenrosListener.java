package com.atenros.eklenti;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.util.UUID;

public class AtenrosListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        File file = new File("plugins/Atenros/players/" + uuid.toString() + ".yml");

        if (file.exists()) {
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            AnaSinif.playerData.put(uuid, data);

            // Verileri Geri Yükle
            p.setHealth(data.getDouble("health", 20.0));
            p.setFoodLevel(data.getInt("food", 20));
            p.setExp( (float) data.getDouble("exp", 0.0) );
            
            // Envanteri yükle (basit hali)
            // Not: Envanter için daha gelişmiş "Base64" metodu kullanılır, 
            // şimdilik basit bir "item" kaydı örneği:
            if(data.contains("inventory")) {
                p.getInventory().setContents(((java.util.List<ItemStack>) data.getList("inventory")).toArray(new ItemStack[0]));
            }

            // Para Yükle (Vault varsa)
            if (AnaSinif.economy != null) {
                double para = data.getDouble("money", 0.0);
                AnaSinif.economy.depositPlayer(p, para);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        YamlConfiguration data = AnaSinif.playerData.get(uuid);

        // Verileri Kaydet
        data.set("health", p.getHealth());
        data.set("food", p.getFoodLevel());
        data.set("exp", p.getExp());
        data.set("inventory", p.getInventory().getContents());
        
        // Öldürdüğü yaratık sayısı (Bunu bir yerde artırıyorsan buraya kaydet)
        data.set("mobKills", data.getInt("mobKills", 0)); 

        if (AnaSinif.economy != null) {
            data.set("money", AnaSinif.economy.getBalance(p));
        }

        AnaSinif.saveToDisk(uuid);
        AnaSinif.playerData.remove(uuid);
    }
}

