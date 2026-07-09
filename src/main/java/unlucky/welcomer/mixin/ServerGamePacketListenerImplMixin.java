package unlucky.welcomer.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import unlucky.welcomer.UnluckySMPWelcomer;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

	// removePlayerFromWorld broadcasts the "multiplayer.player.left"
	// translatable; swap it for our own message.
	@ModifyArg(
			method = "removePlayerFromWorld",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
			),
			index = 0
	)
	private Component welcomer$replaceLeaveMessage(Component original) {
		if (original.getContents() instanceof TranslatableContents translatable
				&& translatable.getKey().equals("multiplayer.player.left")
				&& translatable.getArgs().length > 0
				&& translatable.getArgs()[0] instanceof Component playerName) {
			return UnluckySMPWelcomer.leaveMessage(playerName);
		}
		return original;
	}
}
