package unlucky.welcomer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WelcomerConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("unluckysmp-welcomer.json");

	// & color codes (&7 gray, &a green, &c red, &f white, ...), %name% = player
	public String join_message = "&7[&a+&7] &f%name% joined the Unlucky SMP!";
	public String leave_message = "&7[&c-&7] &f%name% left the Unlucky SMP :(";

	public static WelcomerConfig load() {
		try {
			if (Files.exists(PATH)) {
				try (var reader = Files.newBufferedReader(PATH)) {
					WelcomerConfig config = GSON.fromJson(reader, WelcomerConfig.class);
					if (config != null) {
						return config;
					}
				}
			}
		} catch (IOException | JsonParseException e) {
			UnluckySMPWelcomer.LOGGER.error("Failed to read {}, keeping defaults (file left untouched)", PATH, e);
			return new WelcomerConfig();
		}
		WelcomerConfig config = new WelcomerConfig();
		config.save();
		return config;
	}

	public void save() {
		try {
			Files.writeString(PATH, GSON.toJson(this));
		} catch (IOException e) {
			UnluckySMPWelcomer.LOGGER.error("Failed to write {}", PATH, e);
		}
	}

	/** Renders a template with &-color codes and the %name% placeholder. */
	public static Component format(String template, Component playerName) {
		MutableComponent out = Component.empty();
		ChatFormatting color = ChatFormatting.WHITE;
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < template.length(); i++) {
			if (template.charAt(i) == '&' && i + 1 < template.length()) {
				ChatFormatting next = ChatFormatting.getByCode(template.charAt(i + 1));
				if (next != null) {
					flush(out, buf, color);
					color = next;
					i++;
					continue;
				}
			}
			if (template.startsWith("%name%", i)) {
				flush(out, buf, color);
				out.append(playerName.copy().withStyle(color));
				i += "%name%".length() - 1;
				continue;
			}
			buf.append(template.charAt(i));
		}
		flush(out, buf, color);
		return out;
	}

	private static void flush(MutableComponent out, StringBuilder buf, ChatFormatting color) {
		if (buf.length() > 0) {
			out.append(Component.literal(buf.toString()).withStyle(color));
			buf.setLength(0);
		}
	}
}
