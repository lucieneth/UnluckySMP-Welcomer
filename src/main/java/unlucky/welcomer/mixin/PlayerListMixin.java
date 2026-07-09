package unlucky.welcomer.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import unlucky.welcomer.UnluckySMPWelcomer;

@Mixin(PlayerList.class)
public class PlayerListMixin {

	// placeNewPlayer broadcasts the "multiplayer.player.joined[.renamed]"
	// translatable; swap it for our own message. The player's display name
	// is recovered from the translation args, so no extra capture is needed.
	@ModifyArg(
			method = "placeNewPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
			),
			index = 0
	)
	private Component welcomer$replaceJoinMessage(Component original) {
		if (original.getContents() instanceof TranslatableContents translatable
				&& translatable.getKey().startsWith("multiplayer.player.joined")
				&& translatable.getArgs().length > 0
				&& translatable.getArgs()[0] instanceof Component playerName) {
			return UnluckySMPWelcomer.joinMessage(playerName);
		}
		return original;
	}

	// The vanilla broadcast happens before the joining player is added to the
	// player list, so they never see their own join message. Send it to them
	// directly once they are fully connected.
	@Inject(method = "placeNewPlayer", at = @At("TAIL"))
	private void welcomer$greetJoiningPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
		player.sendSystemMessage(UnluckySMPWelcomer.joinMessage(player.getDisplayName()));
	}
}
