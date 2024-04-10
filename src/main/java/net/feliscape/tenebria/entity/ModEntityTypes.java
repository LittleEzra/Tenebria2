package net.feliscape.tenebria.entity;

import net.feliscape.tenebria.Tenebria;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Tenebria.MOD_ID);

    /* Registering an Entity:
    public static final RegistryObject<EntityType<ENTITY>> TWILIGHT_JELLYFISH = ENTITY_TYPES.register("ENTITY",
            () -> EntityType.Builder.of(ENTITY::new, MobCategory.AMBIENT).sized(1.0f, 1.0f)
                    .build(new ResourceLocation(Template.MOD_ID, "ENTITY").toString()));
                    */

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
