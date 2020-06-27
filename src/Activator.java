import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import stockmarketui.simengine.SimulatorEngine;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		SimulatorEngine se = SimulatorEngine.getInstance();
		String[] args = {};
		se.init(args);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
