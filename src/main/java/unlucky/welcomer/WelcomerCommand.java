package unlucky.welcomer;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class WelcomerCommand {
	private WelcomerCommand() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("welcomer")
				.executes(ctx -> {
					ctx.getSource().sendSuccess(WelcomerCommand::info, false);
					return 1;
				})
				.then(Commands.literal("reload")
						.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
						.executes(ctx -> {
							UnluckySMPWelcomer.CONFIG = WelcomerConfig.load();
							ctx.getSource().sendSuccess(
									() -> Component.literal("Welcomer config reloaded.").withStyle(ChatFormatting.GREEN), true);
							return 1;
						})));
	}

	private static Component info() {
		String version = FabricLoader.getInstance().getModContainer(UnluckySMPWelcomer.MOD_ID)
				.map(mod -> mod.getMetadata().getVersion().getFriendlyString()).orElse("?");
		return Component.literal("UnluckySMP Welcomer v" + version).withStyle(ChatFormatting.GOLD)
				.append(Component.literal("\nCustom join & leave messages for the Unlucky SMP.").withStyle(ChatFormatting.WHITE))
				.append(Component.literal("\nMade by ").withStyle(ChatFormatting.GRAY))
				.append(Component.literal("Lucien").withStyle(ChatFormatting.AQUA))
				.append(Component.literal(" & ").withStyle(ChatFormatting.GRAY))
				.append(Component.literal("Claude").withStyle(ChatFormatting.LIGHT_PURPLE))
				.append(Component.literal("\nConfig: ").withStyle(ChatFormatting.GRAY))
				.append(Component.literal("config/unluckysmp-welcomer.json").withStyle(ChatFormatting.WHITE))
				.append(Component.literal(" — apply changes with ").withStyle(ChatFormatting.GRAY))
				.append(Component.literal("/welcomer reload").withStyle(ChatFormatting.YELLOW));
	}
}
