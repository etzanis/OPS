package preferences;

import java.util.HashMap;
import org.eclipse.jface.preference.IPreferenceStore;
import org.ops.MyOpsNodePlugin;

public class PreferenceConstants
{

  public static final HashMap<String, String> DEFAULT_CSV_COL_DELIMITERS;
  public static final HashMap<String, String> DEFAULT_CSV_ROW_DELIMITERS;
  
  static
  {
    DEFAULT_CSV_COL_DELIMITERS = new HashMap();
    DEFAULT_CSV_COL_DELIMITERS.put(", (comma)", ",");
    DEFAULT_CSV_COL_DELIMITERS.put("; (semicolon)", ";");
    DEFAULT_CSV_COL_DELIMITERS.put("\\t (tab)", "\\t");
    DEFAULT_CSV_COL_DELIMITERS.put("\\s (white space)", " ");
    
    DEFAULT_CSV_ROW_DELIMITERS = new HashMap();
    DEFAULT_CSV_ROW_DELIMITERS.put("\\n", "\n");
    DEFAULT_CSV_ROW_DELIMITERS.put("\\r", "\r");
    DEFAULT_CSV_ROW_DELIMITERS.put("\\r\\n", "\r\n");
  }
  
  private static IPreferenceStore getPreferenceStore()
  {
    return MyOpsNodePlugin.getDefault().getPreferenceStore();
  }
  
 
}
