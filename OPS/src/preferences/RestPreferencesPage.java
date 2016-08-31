package preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.ops.ConnectorNodePlugin;

public abstract class RestPreferencesPage
  extends PreferencePage
  implements IWorkbenchPreferencePage
{
  protected final int numberColumns;
  protected Composite parent;
  
  public RestPreferencesPage(int numberCols)
  {
    this.numberColumns = numberCols;
    setPreferenceStore(ConnectorNodePlugin.getDefault().getPreferenceStore());
  }
  
  public void init(IWorkbench workbench) {}
  
  protected Control createContents(Composite parentComposite)
  {
    this.parent = parentComposite;
    GridLayout gridLayout = new GridLayout(this.numberColumns, false);
    this.parent.setLayout(gridLayout);
    addPageContent();
    initializeValues();
    return new Composite(this.parent, 0);
  }
  
  protected abstract void addPageContent();
  
  protected abstract void initializeValues();
  
  public IPreferenceStore getPreferenceStore()
  {
    return ConnectorNodePlugin.getDefault().getPreferenceStore();
  }
  
  protected void addSpacer()
  {
    addEmptyCells(this.numberColumns);
  }
  
  protected void addEmptyCells(int count)
  {
    for (int i = 0; i < count; i++) {
      new Label(this.parent, 16384).setText("");
    }
  }
  
  protected abstract void performDefaults();
  
  public abstract boolean performOk();
  
  public String buildStorageString(List swtList)
  {
    StringBuilder builder = new StringBuilder();
    
    String[] values = swtList.getItems();
    for (int i = 0; i < values.length; i++)
    {
      String value = values[i];
      builder.append(value);
      if (i + 1 != values.length) {
        builder.append("--");
      }
    }
    return builder.toString();
  }
}
