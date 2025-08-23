package command.component

import command.Command
import command.Environment

class BlueComponent(val operand: String, val result: String): Command {
    override suspend fun execute(environment: Environment): Result<Unit> = runCatching {
        val image = environment.getImage(operand);
        requireNotNull(image){"The image $operand does not exist."}
        val blueComponent = image.blueComponent();
        requireNotNull(blueComponent){"The image $operand does not have a blue channel."}
        environment.putImage(result, blueComponent)
    }
}