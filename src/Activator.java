import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import stockmarketui.simengine.SimulatorEngine;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		SimulatorEngine.getInstance();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		SimulatorEngine.getInstance().shutdown();
	}

}
