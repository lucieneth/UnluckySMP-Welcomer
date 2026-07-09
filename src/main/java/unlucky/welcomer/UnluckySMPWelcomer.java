package unlucky.welcomer;

import net.fabricmc.api.ModInitializer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnluckySMPWelcomer implements ModInitializer {
	public static final String MOD_ID = "unluckysmp-welcomer";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** Live config; replaced wholesale by /welcomer reload. */
	public static volatile WelcomerConfig CONFIG = new WelcomerConfig();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CONFIG = WelcomerConfig.load();
		LOGGER.info("UnluckySMP Welcomer loaded.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	/**
	 * Builds the message broadcast in place of the vanilla
	 * "<name> joined the game". Edit this to change the message.
	 */
	public static Component joinMessage(Component playerName) {
		return WelcomerConfig.format(CONFIG.join_message, playerName);
	}

	/**
	 * Builds the message broadcast in place of the vanilla
	 * "<name> left the game". Edit this to change the message.
	 */
	public static Component leaveMessage(Component playerName) {
		return WelcomerConfig.format(CONFIG.leave_message, playerName);
	}
}
