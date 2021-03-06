package ca.lukegrahamlandry.mercenaries.client.gui;

import ca.lukegrahamlandry.mercenaries.MercenariesMain;
import ca.lukegrahamlandry.mercenaries.entity.LeaderEntity;
import ca.lukegrahamlandry.mercenaries.init.NetworkInit;
import ca.lukegrahamlandry.mercenaries.network.BuyNewMercPacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MercenaryLeaderScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MercenariesMain.MOD_ID, "textures/container/blank.png");

    private final LeaderEntity merc;
    private final int dialogueCount;
    private final boolean isFirstInteraction;
    private final PlayerEntity player;
    private final int guiLeft = 0;
    private final int guiTop = 0;

    private float xMouse;
    private float yMouse;
    private int price;
    private int imageWidth = 255;
    private int imageHeight = 255;
    private Button hireButton;

    List<TextComponent> dialogue = new ArrayList<>();
    List<Integer> dialogueIndexes = new ArrayList<>(); // dont repeat

    public MercenaryLeaderScreen(PlayerEntity player, LeaderEntity merc, int price, int dialogueCount, boolean isFirstInteraction) {
        super(new StringTextComponent("Merc Leader"));
        this.player = player;
        this.merc = merc;
        this.dialogueCount = dialogueCount;
        this.isFirstInteraction = isFirstInteraction;
        this.passEvents = false;
        this.price = price;
    }

    @Override
    protected void init() {
        super.init();
        String text = this.price != Integer.MAX_VALUE ? "Buy New Merc (" + price + ")" : "Cannot Hire Another";
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int buttonWidth = 150;
        int xStart = i + ((this.imageWidth - buttonWidth) / 2);
        int yStart = j + 30;

        this.hireButton = new Button(xStart, yStart, buttonWidth, 20, new StringTextComponent(text), (p_214318_1_) -> {
            if (this.hireButton.active) {
                NetworkInit.INSTANCE.sendToServer(new BuyNewMercPacket());
                Minecraft.getInstance().setScreen(null);
            }
        });
        hireButton.active = this.price != Integer.MAX_VALUE;
        this.addButton(hireButton);

        if (this.dialogueCount > 0){
            this.addButton(new Button(xStart, yStart + 30, buttonWidth, 20, new StringTextComponent("Tell Me About Yourself"), (p_214318_1_) -> {
                this.onDialogueButton();
            }));
        }
    }

    private void onDialogueButton(){
        if (dialogueIndexes.isEmpty()){
            for (int I=0;I<this.dialogueCount;I++){
                dialogueIndexes.add(I);
            }
            Collections.shuffle(dialogueIndexes);
        }

        String dialogueType = this.isFirstInteraction ? "firstLeader" : "leader";
        int indexI = merc.getRandom().nextInt(dialogueIndexes.size());
        int index = dialogueIndexes.get(indexI);
        dialogueIndexes.remove(indexI);
        TranslationTextComponent fullText = new TranslationTextComponent("mercenaries.dialogue." + dialogueType + "_" + index);
        String[] words = fullText.getString().split(" ");
        StringBuilder line = new StringBuilder();
        this.dialogue.clear();
        for (String word : words){
            String lastLine = line.toString();
            line.append(" ").append(word);
            if (this.font.width(line.toString()) > (imageWidth - 20)){
                this.dialogue.add(new StringTextComponent(lastLine));
                line = new StringBuilder(word);
            }
        }
        this.dialogue.add(new StringTextComponent(line.toString()));
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.xMouse = (float)p_230430_2_;
        this.yMouse = (float)p_230430_3_;

        int y = 0;
        for (TextComponent text : this.dialogue){
            this.write(matrixStack, text, i + 10, j + 90 + y);
            y += 12;
        }
    }

    private void write(MatrixStack matrixStack, TextComponent text, int x, int y){
        this.font.draw(matrixStack, text, x, y, 0xFFFFFF);
    }
}
