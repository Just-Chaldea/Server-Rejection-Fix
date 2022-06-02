package gg.chaldea.serene.seasons.rejection.mixin;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sereneseasons.handler.PacketHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mixin(PacketHandler.class)
public class MixinPacketHandler {

	@Inject(method = "<clinit>", at = @At("HEAD"), remap = false, cancellable = true)
	private static void onStaticInit(CallbackInfo ci) {
		try {
			Field STABLE = PacketHandler.class.getDeclaredField("HANDLER");
			STABLE.setAccessible(true);

			flipVariableFinality(STABLE, () -> {
				try {
					NetworkRegistry.ChannelBuilder var10000 = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("sereneseasons", "main_channel"))
							.networkProtocolVersion(() -> "0")
							.serverAcceptedVersions("0"::equals)
							.clientAcceptedVersions(serverVersion -> {
						if (serverVersion.equalsIgnoreCase(NetworkRegistry.ABSENT) || serverVersion.equalsIgnoreCase(NetworkRegistry.ACCEPTVANILLA)) {
							return true;
						}
						return "0".equals(serverVersion);
					});

					STABLE.set(null, var10000.simpleChannel());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			});
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		ci.cancel();
	}

	private static void flipVariableFinality(Field field) throws NoSuchFieldException, IllegalAccessException {
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	}

	private static void flipVariableFinality(Field field, Runnable runnable) throws NoSuchFieldException, IllegalAccessException {
		flipVariableFinality(field);
		runnable.run();
		flipVariableFinality(field);
	}
}
