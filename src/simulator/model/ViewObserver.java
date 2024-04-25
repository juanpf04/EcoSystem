package simulator.model;

public interface ViewObserver {
	
	void onDarkMode();

	void onLightMode();

	void onSpectacularView();

	void onStandardView();
}
