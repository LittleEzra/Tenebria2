package net.feliscape.tenebria.advancements;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.datagen.advancements.ModAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ModAdvancement {

    static final ResourceLocation BACKGROUND = Tenebria.asResource("textures/gui/advancements.png");
    static final String LANG = "advancement." + Tenebria.MOD_ID + ".";
    static final String SECRET_SUFFIX = "\n\u00A77(Hidden Advancement)";

    private Advancement.Builder builder;
    private ModAdvancement parent;

    Advancement datagenResult;

    private String id;
    private String subfolder = "";
    private String title;
    private String description;

    public ModAdvancement(String id, UnaryOperator<Builder> b) {
        this.builder = Advancement.Builder.advancement();
        this.id = id;

        Builder t = new Builder();
        b.apply(t);

        builder.display(t.icon, Component.translatable(titleKey()),
                Component.translatable(descriptionKey()).withStyle(s -> s.withColor(0xDBA213)),
                id.equals("root") ? BACKGROUND : null, t.type.frame, t.type.toast, t.type.announce, t.type.hide);

        ModAdvancements.ENTRIES.add(this);
    }
    public ModAdvancement(String subfolder, String id, UnaryOperator<Builder> b) {
        this.builder = Advancement.Builder.advancement();
        this.id = id;
        this.subfolder = subfolder;

        Builder t = new Builder();
        b.apply(t);

        builder.display(t.icon, Component.translatable(titleKey()),
                Component.translatable(descriptionKey()).withStyle(s -> s.withColor(0xDBA213)),
                id.equals("root") ? BACKGROUND : null, t.type.frame, t.type.toast, t.type.announce, t.type.hide);

        ModAdvancements.ENTRIES.add(this);
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }

    public String titleKey() {
        return LANG + id;
    }

    public String descriptionKey() {
        return titleKey() + ".description";
    }

    public boolean isAlreadyAwardedTo(Player player) {
        if (!(player instanceof ServerPlayer sp))
            return true;
        Advancement advancement = sp.getServer()
                .getAdvancements()
                .getAdvancement(Tenebria.asResource(id));
        if (advancement == null)
            return true;
        return sp.getAdvancements()
                .getOrStartProgress(advancement)
                .isDone();
    }

    public void save(Consumer<Advancement> t) {
        if (parent != null)
            builder.parent(parent.datagenResult);
        datagenResult = builder.save(t, Tenebria.asResource(subfolder.isEmpty() ? id : subfolder + "/" + id)
                .toString());
    }

    void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(titleKey(), title);
        consumer.accept(descriptionKey(), description);
    }

    public static enum TaskType {

        SILENT(FrameType.TASK, false, false, false),
        NORMAL(FrameType.TASK, true, false, false),
        NOISY(FrameType.TASK, true, true, false),
        EXPERT(FrameType.GOAL, true, true, false),
        SECRET(FrameType.GOAL, true, true, true),
        CHALLENGE(FrameType.CHALLENGE, true, true, false),
        SECRET_CHALLENGE(FrameType.CHALLENGE, true, true, true),

        ;

        private FrameType frame;
        private boolean toast;
        private boolean announce;
        private boolean hide;

        private TaskType(FrameType frame, boolean toast, boolean announce, boolean hide) {
            this.frame = frame;
            this.toast = toast;
            this.announce = announce;
            this.hide = hide;
        }
    }

    public class Builder {

        private TaskType type = TaskType.NORMAL;
        private int keyIndex;
        private ItemStack icon;


        public Builder special(TaskType type) {
            this.type = type;
            return this;
        }

        public Builder after(ModAdvancement other) {
            ModAdvancement.this.parent = other;
            return this;
        }

        public Builder icon(ItemLike item) {
            return icon(new ItemStack(item));
        }

        public Builder icon(ItemStack stack) {
            icon = stack;
            return this;
        }

        public Builder title(String title) {
            ModAdvancement.this.title = title;
            return this;
        }

        public Builder description(String description) {
            ModAdvancement.this.description = description;
            return this;
        }


        public Builder when(AbstractCriterionTriggerInstance triggerInstance) {
            return externalTrigger(triggerInstance);
        }
        public Builder whenGetIcon() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(icon.getItem()));
        }
        public Builder whenGet(ItemLike... items) {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
        }
        public Builder whenGotAll(ItemLike... items) {
            for(ItemLike item : items) {
                externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(item));
            }
            return this;
        }
        public Builder whenConsume(ItemLike... items) {
            return externalTrigger(ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(items).build()));
        }

        public Builder whenConsumedAll(List<ItemLike> list) {
            for(ItemLike item : list) {
                externalTrigger(ConsumeItemTrigger.TriggerInstance.usedItem(item));
            }
            return this;
        }
        public Builder whenConsumedAll(ItemLike... items) {
            for(ItemLike item : items) {
                externalTrigger(ConsumeItemTrigger.TriggerInstance.usedItem(item));
            }
            return this;
        }

        public Builder awardedForFree() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] {}));
        }

        public Builder externalTrigger(CriterionTriggerInstance trigger) {
            builder.addCriterion(String.valueOf(keyIndex), trigger);
            keyIndex++;
            return this;
        }

        public Builder rewards(AdvancementRewards rewards) {
            builder.rewards(rewards);
            return this;
        }
        public Builder rewards(AdvancementRewards.Builder rewards) {
            builder.rewards(rewards);
            return this;
        }
    }
}
