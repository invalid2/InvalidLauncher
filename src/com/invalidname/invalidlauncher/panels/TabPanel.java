package com.invalidname.invalidlauncher.panels;

import org.liquidengine.legui.component.Panel;

import com.invalidname.invalidlauncher.tabs.DZNewsTab;

public class TabPanel extends Panel {
	//This panel will be the current tab, default is dz news
	Panel tab;
	
	public TabPanel(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setFocusable(false);
	}
	
	public void setCurrentTab(Panel tab) {
		this.remove(this.getCurrentTab());
		this.add(tab);
		this.tab = tab;
	}
	
	public Panel getCurrentTab() {
		return this.tab;
	}
}
