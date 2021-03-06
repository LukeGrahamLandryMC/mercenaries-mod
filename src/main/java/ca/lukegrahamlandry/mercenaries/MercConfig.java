package ca.lukegrahamlandry.mercenaries;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MercConfig {
    public static final ForgeConfigSpec server_config;

    public static void init(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config);
        CommentedFileConfig file = CommentedFileConfig.builder(new File(FMLPaths.CONFIGDIR.get().resolve(MercenariesMain.MOD_ID + ".toml").toString())).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        server_config.setConfig(file);
    }

    public static final ForgeConfigSpec.IntValue foodDecayRate;
    public static final ForgeConfigSpec.IntValue moneyDecayRate;
    public static final ForgeConfigSpec.IntValue maxMercs;
    public static final ForgeConfigSpec.IntValue emeraldValue;
    public static final ForgeConfigSpec.IntValue diamondValue;
    public static final ForgeConfigSpec.IntValue idleFollowDistance;
    public static final ForgeConfigSpec.IntValue fightingFollowDistance;
    public static final ForgeConfigSpec.IntValue teleportDistance;
    public static final ForgeConfigSpec.IntValue basePrice;
    public static final ForgeConfigSpec.DoubleValue priceScaleFactor;
    public static final ForgeConfigSpec.IntValue artifactCooldown;
    public static final ForgeConfigSpec.IntValue artifactUseTime;
    public static final ForgeConfigSpec.BooleanValue generateLeaderHouses;
    public static final ForgeConfigSpec.ConfigValue<String> hirePaymentItem;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> names;
    public static final ForgeConfigSpec.BooleanValue createHorseToRide;
    public static final ForgeConfigSpec.BooleanValue dropItemsOnDeath;
    public static final ForgeConfigSpec.IntValue rehirePrice;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> dialogueLinesCount;

    static {
        final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();

        serverBuilder.comment("Mercenaries server side settings")
                .push("server");

        foodDecayRate = serverBuilder
                .comment("How many ticks for a mercenary to consume 1 unit of food (a full bar is 20 units). Use a value of 2147483647 to disable food requirement")
                .defineInRange("foodDecayRate", 24000 / 20 * 4, 1, Integer.MAX_VALUE);

        moneyDecayRate = serverBuilder
                .comment("How many ticks for a mercenary to consume 1 unit of money (a full bar is 20 units). Use a value of 2147483647 to disable payment requirement")
                .defineInRange("moneyDecayRate", 24000 / 20 * 4, 1, Integer.MAX_VALUE);

        maxMercs = serverBuilder
                .comment("How many mercenaries you can have hired at once")
                .defineInRange("maxMercs", 3, 0, Integer.MAX_VALUE);

        emeraldValue = serverBuilder
                .comment("How many money units is an emerald worth. (this will actually be the hirePaymentItem which defaults to emerald)")
                .defineInRange("emeraldValue", 4, 0, Integer.MAX_VALUE);

        diamondValue = serverBuilder
                .comment("How many money units is a diamond worth")
                .defineInRange("diamondValue", 19, 0, Integer.MAX_VALUE);

        idleFollowDistance = serverBuilder
                .comment("When there are no targets, how many blocks should you walk away before your mercenaries start following")
                .defineInRange("idleFollowDistance", 8, 1, Integer.MAX_VALUE);

        fightingFollowDistance = serverBuilder
                .comment("When there are targets, how many blocks should you walk away before your mercenaries start following")
                .defineInRange("fightingFollowDistance", 16, 1, Integer.MAX_VALUE);

        teleportDistance = serverBuilder
                .comment("how many blocks should you walk away before your mercenaries teleport to you")
                .defineInRange("teleportDistance", 24, 1, Integer.MAX_VALUE);

        basePrice = serverBuilder
                .comment("how many emeralds should your first mercenary cost. this will be scaled up for subsequent hires")
                .defineInRange("basePrice", 20, 0, Integer.MAX_VALUE);

        priceScaleFactor = serverBuilder
                .comment("how quickly should the price of mercenaries scale up. base price will be mulitplied by currentlyOwned^priceScaleFactor")
                .defineInRange("priceScaleFactor", 2.0D, 1, Integer.MAX_VALUE);

        artifactCooldown = serverBuilder
                .comment("UNUSED. ticks between a mercenary using any artifacts (in addition to their normal cool down)")
                .defineInRange("artifactCooldown", 100, 1, Integer.MAX_VALUE);

        artifactUseTime = serverBuilder
                .comment("UNUSED. ticks it takes the mercenary to use an artifact")
                .defineInRange("artifactUseTime", 30, 1, Integer.MAX_VALUE);

        hirePaymentItem = serverBuilder
                .comment("the resource location of the item used to pay for new mercenaries")
                .define("hirePaymentItem", "minecraft:emerald");

        names = serverBuilder
                .comment("names to use for mercenaries. these can be translation keys but they don't have to be. one of these will be randomly chosen for each new mercenary")
                .define("names", Arrays.asList("Sir Fight-a-lot", "Fighter McKillerson", "Murder McSlayer", "Sir Stab-a-lot"));

        generateLeaderHouses = serverBuilder
                .comment("should leader houses generate in villages?")
                .define("generateLeaderHouses", true);

        createHorseToRide = serverBuilder
                .comment("if true, when a player rides a horse, their followers will spawn horses to ride (which disappear when player stops riding)")
                .define("createHorseToRide", true);

        dropItemsOnDeath = serverBuilder
                .comment("when a mercenary is killed, should its inventory drop")
                .define("dropItemsOnDeath", true);

        rehirePrice = serverBuilder
                .comment("how much should it cost to rehire a mercenary that has abandoned you. Use -1 to make them just leave forever")
                .defineInRange("rehirePrice", 15, -1, Integer.MAX_VALUE);
        dialogueLinesCount = serverBuilder
                .comment("how many dialogue lines are defined for each category. [firstLeader, leader] the text will be taken from the lang mercenaries.dialogue.name_0, mercenaries.dialogue.name_1, etc")
                .define("dialogueLinesCount", Arrays.asList(1, 2));


        server_config = serverBuilder.build();
    }

    public static int getFoodDecayRate(){
        return foodDecayRate.get();
    }

    public static int getMoneyDecayRate(){
        return moneyDecayRate.get();
    }

    public static int maxMercs(){
        return maxMercs.get();
    }

    public static Item buyMercItem(){
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(hirePaymentItem.get()));
    }

    public static int getMoneyValue(Item item){
        if (item == buyMercItem()) return emeraldValue.get();
        if (item == Items.DIAMOND) return diamondValue.get();

        return 0;
    }

    public static boolean artifactsInstalled(){
        return isModLoaded("dungeons_gear");
    }

    private static boolean isModLoaded(String modID) {
        return ModList.get() != null && ModList.get().getModContainerById(modID).isPresent();
    }

    public static int getSharedArtifactCooldown() {
        return artifactCooldown.get();
    }

    public static int getTimeToUseArtifact() {
        return artifactUseTime.get();
    }

    public static int caclualteCurrentPrice(ServerPlayerEntity player) {
        int owned = SaveMercData.get().getMercs(player).size();
        if (owned >= maxMercs()) return Integer.MAX_VALUE;
        return (int) (basePrice.get() * Math.pow(priceScaleFactor.get(), owned));
    }

    public static double getIdleFollowDistance() {
        return Math.pow(idleFollowDistance.get(), 2);
    }

    public static double getFightingFollowDistance() {
        return Math.pow(fightingFollowDistance.get(), 2);
    }

    public static double getTeleportDistance() {
        return Math.pow(teleportDistance.get(), 2);
    }
}
