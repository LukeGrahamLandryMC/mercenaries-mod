package ca.lukegrahamlandry.mercenaries.client.container;

import ca.lukegrahamlandry.mercenaries.MercenariesMain;
import ca.lukegrahamlandry.mercenaries.entity.MercenaryEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MercenaryScreen extends ContainerScreen<MerceneryContainer> {
    private static final ResourceLocation GUARD_GUI_TEXTURES_NO_SWORD = new ResourceLocation(MercenariesMain.MOD_ID, "textures/container/inventory_old.png");
    private static final ResourceLocation GUARD_GUI_TEXTURES = new ResourceLocation(MercenariesMain.MOD_ID, "textures/container/inventory.png");
    private final MercenaryEntity merc;

    private float xMouse;
    private float yMouse;

    public MercenaryScreen(MerceneryContainer container, PlayerInventory playerInventory, MercenaryEntity merc) {
        super(container, playerInventory, merc.getDisplayName());
        this.merc = merc;
        this.passEvents = false;
    }

    protected void renderLabels(MatrixStack matrixStack, int p_230451_2_, int p_230451_3_) {
        // super.renderLabels(matrixStack, p_230451_2_, p_230451_3_);

        int health = MathHelper.ceil(merc.getHealth());
        int armor = merc.getArmorValue();
        ModifiableAttributeInstance attrMaxHealth = merc.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float)attrMaxHealth.getValue();
        float absorb = MathHelper.ceil(merc.getAbsorptionAmount());
        int food = merc.getFood();
        int money = merc.getMoney();

        BarRenderHelper.renderArmor(matrixStack, 0-90, 0 -15+50, armor, this::blit);
        BarRenderHelper.renderHealth(0-90, 0-10 -15+50, matrixStack, health, healthMax, absorb, this::blit);
        BarRenderHelper.renderFood(0-90, 0-20 -15+50, matrixStack, food, this::blit);
        BarRenderHelper.renderMoney(0-90, 0-30 -15+50, money);
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.xMouse = (float)p_230430_2_;
        this.yMouse = (float)p_230430_3_;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        ResourceLocation tex = this.merc.getMainHandItem().isEmpty() ? GUARD_GUI_TEXTURES : GUARD_GUI_TEXTURES_NO_SWORD;
        this.minecraft.getTextureManager().bind(tex);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        InventoryScreen.renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, this.merc);
    }
}
