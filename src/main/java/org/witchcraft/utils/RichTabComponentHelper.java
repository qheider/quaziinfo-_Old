package org.witchcraft.utils;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.component.html.HtmlTabPanel;

@Name("richTabComponentHelper")
@Scope(ScopeType.SESSION)
public class RichTabComponentHelper {
	
	 private HtmlTabPanel tabPanel;
	 private String selectedTab;

	 public void setTabPanel(HtmlTabPanel tabPanel) {
	  this.tabPanel = tabPanel;
	 }

	 public HtmlTabPanel getTabPanel() {
	  return tabPanel;
	 }

	 public void setSelectedTab(String selectedTab) {
	  this.selectedTab = selectedTab;
	 }

	 public String getSelectedTab() {
	  return selectedTab;
	 }

}
