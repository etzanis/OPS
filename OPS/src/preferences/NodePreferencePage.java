package preferences;

import java.util.HashMap;
import java.util.Set;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NodePreferencePage
  extends RestPreferencesPage
{
  private static final int NUMBER_COLUMNS = 2;
  protected Text apiID;
  protected Text apiKey;
  protected Text swaggerFile; 
  
  public NodePreferencePage()
  {
    super(2);
  }
  
  protected void addPageContent()
  {
    GridData spanAll = new GridData();
    spanAll.horizontalSpan = 2;
    Label desc = new Label(this.parent, 2048);
    desc.setText("OpenPhacts Details");
    
    desc.setLayoutData(spanAll);
    
    GridData spanOne = new GridData();
    spanOne.horizontalSpan = 1;
    spanOne.horizontalAlignment = 4;
    addSpacer();
    
    new Label(this.parent, 2048).setText("app_id:");
    this.apiID = new Text(this.parent, 18436);
    this.apiID.setLayoutData(spanOne);

    addSpacer();
    
    new Label(this.parent, 2048).setText("app_key:");
    this.apiKey = new Text(this.parent, 18436);
    this.apiKey.setLayoutData(spanOne);
    
    addSpacer();
    
    new Label(this.parent, 2048).setText("Open Phacts API:");
    this.swaggerFile = new Text(this.parent, 18436);
    this.swaggerFile.setLayoutData(spanOne);
    
    addSpacer();
  }
  
  protected void initializeValues()
  {
    IPreferenceStore store = getPreferenceStore();
    
    String rowDel = store.getString("apiKey");
    this.apiKey.setText(rowDel);
    String quoteChar = store.getString("apiID");
    this.apiID.setText(quoteChar);
    String escapeChar = store.getString("swaggerFile");
    this.swaggerFile.setText(escapeChar);
  }
  
  protected void performDefaults()
  {
    IPreferenceStore store = getPreferenceStore();
    
    
    String rowDel = store.getDefaultString(
      "apiKey");
    this.apiKey.setText(rowDel);

    String quoteChar = store.getDefaultString(
      "apiID");
    this.apiID.setText(quoteChar);
    
    String escapeChar = store.getDefaultString(
      "swaggerFile");
    this.swaggerFile.setText(escapeChar);
  }
  
  public boolean performOk()
  {
    IPreferenceStore store = getPreferenceStore();
        
    store.setValue("apiID", this.apiID.getText());
    store.setValue("apiKey", this.apiKey.getText());
    store.setValue("swaggerFile", this.swaggerFile.getText());
    
    setErrorMessage(null);
    
    return true;
  }
  
  

}
