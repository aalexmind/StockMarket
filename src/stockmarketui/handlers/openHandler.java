package stockmarketui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class openHandler {
	@Execute
	public void execute(Shell shell) {
		FileDialog dialog = new FileDialog(shell);
		dialog.open();
	}

}
