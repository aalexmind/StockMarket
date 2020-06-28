package stockmarketui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import stockmarketui.simengine.SimulatorEngine;

public class exitCommand {
	@Execute
	public void execute(IWorkbench workbench, Shell shell) {
		if (MessageDialog.openConfirm(shell, "Confirmation", "Do you want to exit?")) {
			SimulatorEngine.getInstance().shutdown();
			workbench.close();
		}
	}
}
