xpackage com.atenros.eklenti;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AnaSinif extends JavaPlugin {

    public static HashMap<UUID, YamlConfiguration> playerData = new HashMap<>();
    public static Economy economy = null;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupEconomy();
        }
        getServer().getPluginManager().registerEvents(new AtenrosListener(), this);
        getLogger().info("(Full Versiyon) aktif edildi!");
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) economy = rsp.getProvider();
    }

    public static void saveToDisk(UUID uuid) {
        try {
            AnaSinif.playerData.get(uuid).save(new File("plugins/Atenros/players/" + uuid.toString() + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

