package ca.lukegrahamlandry.mercenaries.events;

import ca.lukegrahamlandry.mercenaries.MercConfig;
import ca.lukegrahamlandry.mercenaries.MercenariesMain;
import ca.lukegrahamlandry.mercenaries.integration.RepurposedStructuresCompat;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

// CREDIT: https://gist.github.com/TelepathicGrunt/4fdbc445ebcbcbeb43ac748f4b18f342

@Mod.EventBusSubscriber(modid = MercenariesMain.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class AddVillagerHouse {
    public static boolean hasDoneLeaderHouse; // used by mixins. just here so its present at runtime

    // if i use PlacementBehaviour.RIDGID they dont spawn. if i use TERRAIN_MATCHING they spawn but colapse in a garbage way.
    // TODO: add one to the list to make them have a cobble platform under like old houses so they dont just float
    public static JigsawPattern.PlacementBehaviour placement = JigsawPattern.PlacementBehaviour.create(MercenariesMain.MOD_ID, MercenariesMain.MOD_ID, ImmutableList.of());


    private static void addBuildingToPool(MutableRegistry<JigsawPattern> templatePoolRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        JigsawPattern pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        // LeaderJigsawPiece is checked by the mixins to make them happen once per village
        SingleJigsawPiece piece = LeaderJigsawPiece.single(nbtPieceRL).apply(placement);

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        List<Pair<JigsawPiece, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }

    @SubscribeEvent
    public static void addNewVillageBuilding(final FMLServerAboutToStartEvent event) {
        if (!MercConfig.generateLeaderHouses.get()) return;

        // the mixins used to make the buildings happen once per village make this value irrelevant
        int weight = 1;

        MutableRegistry<JigsawPattern> templatePoolRegistry = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).get();

        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/plains/houses"), MercenariesMain.MOD_ID + ":plains", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/snowy/houses"), MercenariesMain.MOD_ID + ":snowy", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/taiga/houses"), MercenariesMain.MOD_ID + ":taiga", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/savanna/houses"), MercenariesMain.MOD_ID + ":savana", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/desert/houses"), MercenariesMain.MOD_ID + ":desert", weight);

        // added to terminators as well in case there is not a large enough house spot for a building to spawn. just gives it another chance
        // this feels shitty and maybe unessisary. should revisit
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/plains/terminators"), MercenariesMain.MOD_ID + ":end/plains", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/snowy/terminators"), MercenariesMain.MOD_ID + ":end/snowy", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/taiga/terminators"), MercenariesMain.MOD_ID + ":end/taiga", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/savanna/terminators"), MercenariesMain.MOD_ID + ":end/savana", weight);
        addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/desert/terminators"), MercenariesMain.MOD_ID + ":end/desert", weight);

        if (ModList.get().isLoaded("repurposed_structures")) {
            String rl = "repurposed_structures:village";

            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/mountains/houses"), MercenariesMain.MOD_ID + ":mountains", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/badlands/houses"), MercenariesMain.MOD_ID + ":badlands", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/jungle/houses"), MercenariesMain.MOD_ID + ":jungle", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/swamp/houses"), MercenariesMain.MOD_ID + ":swamp", weight);

            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/birch/houses"), MercenariesMain.MOD_ID + ":plains", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/dark_forest/houses"), MercenariesMain.MOD_ID + ":plains", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/giant_tree_taiga/houses"), MercenariesMain.MOD_ID + ":taiga", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/oak/houses"), MercenariesMain.MOD_ID + ":plains", weight);


            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/mountains/terminators"), MercenariesMain.MOD_ID + ":mountains", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/badlands/terminators"), MercenariesMain.MOD_ID + ":badlands", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/jungle/terminators"), MercenariesMain.MOD_ID + ":jungle", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/swamp/terminators"), MercenariesMain.MOD_ID + ":swamp", weight);

            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/birch/terminators"), MercenariesMain.MOD_ID + ":plains", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/dark_forest/terminators"), MercenariesMain.MOD_ID + ":plains", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/giant_tree_taiga/terminators"), MercenariesMain.MOD_ID + ":taiga", weight);
            addBuildingToPool(templatePoolRegistry, new ResourceLocation(rl + "/oak/terminators"), MercenariesMain.MOD_ID + ":plains", weight);

            RepurposedStructuresCompat.forceHousesInRSVillages();
        }

    }

}
