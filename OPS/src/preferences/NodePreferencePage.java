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
  protected Combo colDelCombo;
  protected Combo rowDelCombo;
  protected Text quoteCharField;
  protected Text escapeCharField;
  private static final HashMap<String, String> COL_DELIMITERS = PreferenceConstants.DEFAULT_CSV_COL_DELIMITERS;
  private static final HashMap<String, String> ROW_DELIMITERS = PreferenceConstants.DEFAULT_CSV_ROW_DELIMITERS;
  
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
    
    new Label(this.parent, 2048).setText("Column delimiter:");
    this.colDelCombo = new Combo(this.parent, 4);
    this.colDelCombo.setItems(getColumnDelimiterOptions());
    this.colDelCombo.setLayoutData(spanOne);
    
    addSpacer();
    
    new Label(this.parent, 2048).setText("Row delimiter:");
    this.rowDelCombo = new Combo(this.parent, 12);
    this.rowDelCombo.setItems(getRowDelimiterOptions());
    this.rowDelCombo.setLayoutData(spanOne);
    
    addSpacer();
    
    new Label(this.parent, 2048).setText("Api Key:");
    this.quoteCharField = new Text(this.parent, 18436);
    this.quoteCharField.setLayoutData(spanOne);
    
    addSpacer();
    
    new Label(this.parent, 2048).setText("Swagger File:");
    this.escapeCharField = new Text(this.parent, 18436);
    this.escapeCharField.setLayoutData(spanOne);
    
    addSpacer();
  }
  
  protected void initializeValues()
  {
    IPreferenceStore store = getPreferenceStore();
    
    String colDel = store.getString("csvColumnDelimiterPreference");
    setColumnDelimiterSelection(colDel);
    String rowDel = store.getString("csvRowDelimiterPreference");
    setRowDelimiterSelection(rowDel);
    
    String quoteChar = store.getString("csvQuoteCharacterPreference");
    this.quoteCharField.setText(quoteChar);
    String escapeChar = store.getString("csvEscapeCharacterPreference");
    this.escapeCharField.setText(escapeChar);
  }
  
  protected void performDefaults()
  {
    IPreferenceStore store = getPreferenceStore();
    
    String colDel = store.getDefaultString(
      "csvColumnDelimiterPreference");
    setColumnDelimiterSelection(colDel);
    
    String rowDel = store.getDefaultString(
      "csvRowDelimiterPreference");
    setRowDelimiterSelection(rowDel);
    
    String quoteChar = store.getDefaultString(
      "csvQuoteCharacterPreference");
    this.quoteCharField.setText(quoteChar);
    
    String escapeChar = store.getDefaultString(
      "csvEscapeCharacterPreference");
    this.escapeCharField.setText(escapeChar);
  }
  
  public boolean performOk()
  {
    IPreferenceStore store = getPreferenceStore();
    
    String colDel = this.colDelCombo.getText();
    if (COL_DELIMITERS.containsKey(colDel)) {
      colDel = (String)COL_DELIMITERS.get(colDel);
    }
    String quoteChar = this.quoteCharField.getText();
    String escapeChar = this.escapeCharField.getText();
    
    HashMap<String, String> charSettings = new HashMap();
    charSettings.put(colDel, "column delimiter");
    charSettings.put(quoteChar, "quote character");
    charSettings.put(escapeChar, "escape character");
    for (String value : charSettings.keySet()) {
      if (value.length() > 1)
      {
        setErrorMessage("The " + (String)charSettings.get(value) + " must not " + 
          "be longer than one character!");
        return false;
      }
    }
    store.setValue("csvColumnDelimiterPreference", colDel);
    store.setValue("csvRowDelimiterPreference", 
      this.rowDelCombo.getText());
    store.setValue("csvQuoteCharacterPreference", quoteChar);
    store.setValue("csvEscapeCharacterPreference", escapeChar);
    
    setErrorMessage(null);
    
    return true;
  }
  
  private void setColumnDelimiterSelection(String colDel)
  {
    String[] defaultOptions = getColumnDelimiterOptions();
    boolean optionFound = false;
    for (int i = 0; i < defaultOptions.length; i++)
    {
      String displayName = defaultOptions[i];
      String value = (String)COL_DELIMITERS.get(displayName);
      if (colDel.equals(value))
      {
        this.colDelCombo.select(i);
        optionFound = true;
        break;
      }
    }
    if (!optionFound)
    {
      String[] options = new String[COL_DELIMITERS.size() + 1];
      options[0] = colDel;
      for (int c = 0; c < defaultOptions.length; c++)
      {
        String displayName = defaultOptions[c];
        options[(c + 1)] = displayName;
      }
      this.colDelCombo.setItems(options);
      this.colDelCombo.select(0);
    }
  }
  
  private String[] getColumnDelimiterOptions()
  {
    return (String[])COL_DELIMITERS.keySet().toArray(new String[COL_DELIMITERS.size()]);
  }
  
  private void setRowDelimiterSelection(String rowDel)
  {
    String[] options = getRowDelimiterOptions();
    boolean optionFound = false;
    for (int i = 0; i < options.length; i++)
    {
      String displayName = options[i];
      String value = (String)ROW_DELIMITERS.get(displayName);
      if ((rowDel.equals(value)) || (rowDel.equals(displayName)))
      {
        this.rowDelCombo.select(i);
        optionFound = true;
        break;
      }
    }
    if (!optionFound) {
      setErrorMessage("Unknown row delimiter: " + rowDel);
    }
  }
  
  private String[] getRowDelimiterOptions()
  {
    return (String[])ROW_DELIMITERS.keySet().toArray(new String[ROW_DELIMITERS.size()]);
  }
}
