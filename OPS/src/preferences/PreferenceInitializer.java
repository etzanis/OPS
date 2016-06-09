package preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.ops.MyOpsNodePlugin;

public class PreferenceInitializer
  extends AbstractPreferenceInitializer
{
  public void initializeDefaultPreferences()
  {
    IPreferenceStore store = MyOpsNodePlugin.getDefault().getPreferenceStore();
    store.setDefault("enableLoggingPreference", false);
    store.setDefault("loggingPreference", "");
    store.setDefault("authenticationProcedurePreference", 
      "None");
    store.setDefault("usernamePreference", "");
    store.setDefault("passwordPreference", "");
    store.setDefault("responseCellTypePreference", 
      "Autodetection");
    store.setDefault("dataformatParsingPreference", 
      "Autodetect (header data)");
    
    store.setDefault("csvColumnDelimiterPreference", ",");
    store.setDefault("csvQuoteCharacterPreference", "\"");
    store.setDefault("csvRowDelimiterPreference", "\n");
    store.setDefault("csvEscapeCharacterPreference", "");
    
    store.setDefault("atomElementSeparator", ".");
    store.setDefault("atomRemoveNamespace", true);
    
    store.setDefault("jsonElementSeparator", ".");
    store.setDefault("jsonUseRootElementPreference", false);
    store.setDefault("jsonRootElementNamePreference", "resources");
    
    store.setDefault("xmlElementSeparatorPreference", ".");
    store.setDefault("xmlRootElementNamePreference", "resources");
    store.setDefault("xmlRemoveNamespacePreference", true);
    store.setDefault("xmlUseResourceTagNamePreference", false);
    store.setDefault("xmlResourceTagNamePreference", "resource");
  }
  
  
  private String getValuesAsString(HashMap<Integer, String> values)
  {
    StringBuilder builder = new StringBuilder();
    SortedSet<Integer> indices = 
      new TreeSet(values.keySet());
    
    int lastIndex = indices.size() - 1;
    for (Iterator localIterator = indices.iterator(); localIterator.hasNext();)
    {
      int i = ((Integer)localIterator.next()).intValue();
      String value = (String)values.get(Integer.valueOf(i));
      builder.append(value);
      if (i != lastIndex) {
        builder.append("--");
      }
    }
    return builder.toString();
  }
}
