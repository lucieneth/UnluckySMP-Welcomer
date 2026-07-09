package unlucky.welcomer.mixin;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import unlucky.welcomer.WelcomerCommand;

@Mixin(Commands.class)
public class CommandsMixin {

	// Register /welcomer after vanilla commands are set up. Runs on every
	// dispatcher rebuild (server start and /reload), so it stays registered.
	@Inject(method = "<init>", at = @At("RETURN"))
	private void welcomer$registerCommands(Commands.CommandSelection selection, CommandBuildContext context, CallbackInfo ci) {
		WelcomerCommand.register(((Commands) (Object) this).getDispatcher());
	}
}
