package medipro.commands;

import medipro.IKeyAction;

public class UnhookCommand extends Command {

    public static final String UNHOOK_REGEX = "^unhook$";

    public UnhookCommand() {
        super(UNHOOK_REGEX);
    }

    @Override
    public void execute(IKeyAction action, String rawText) throws InterruptedException {
        action.addKey("j");
        sleep(10);
        action.removeKey("j");
    }

}
