package io.th0rgal.oraxen.mechanics.provided.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.mechanics.Mechanic;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.pack.ResourcePack;
import io.th0rgal.oraxen.utils.Logs;
import io.th0rgal.oraxen.utils.Utils;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.*;

public class BlockMechanicFactory extends MechanicFactory {

    private static List<JsonObject> mushroomStemBlockstateOverrides = new ArrayList<>();

    public BlockMechanicFactory(ConfigurationSection section) {
        super(section);

        // this modifier should be executed when all the items have been parsed, just before zipping the pack
        ResourcePack.addModifiers(packFolder -> {
            File blockstatesFolder = new File(packFolder, "blockstates");
            if (!blockstatesFolder.exists())
                blockstatesFolder.mkdirs();
            Logs.log("used blockstates:" + getBlockstateContent());
            File file = new File(blockstatesFolder, "mushroom_stem.json");
            Utils.writeStringToFile(file, getBlockstateContent());
        });

        MechanicsManager.registerListeners(OraxenPlugin.get(), new BlockMechanicsManager(this));
    }

    private String getBlockstateContent() {
        JsonObject mushroomStem = new JsonObject();
        JsonArray multipart = new JsonArray();
        //adds default override
        multipart.add(getBlockstateOverride("mushroom_stem", 15));
        for (JsonObject override : mushroomStemBlockstateOverrides)
            multipart.add(override);
        Logs.log(multipart.toString());
        mushroomStem.add("multipart", multipart);
        return mushroomStem.toString();
    }

    public static JsonObject getBlockstateOverride(String modelName, int when) {
        JsonObject content = new JsonObject();
        JsonObject model = new JsonObject();
        model.addProperty("model", modelName);
        content.add("apply", model);
        content.add("when", Utils.getBlockstateWhenFields(when));
        return content;
    }

    @Override
    public Mechanic parse(ConfigurationSection itemMechanicConfiguration) {
        BlockMechanic mechanic = new BlockMechanic(this, itemMechanicConfiguration);
        mushroomStemBlockstateOverrides.add
                (getBlockstateOverride(mechanic.getModel(), mechanic.getCustomVariation()));
        Logs.log("new mushroom stem:" + mushroomStemBlockstateOverrides.toString());
        addToImplemented(mechanic);
        return mechanic;
    }

}