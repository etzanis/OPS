package preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.ops.ConnectorNodePlugin;

public class PreferenceInitializer
  extends AbstractPreferenceInitializer
{
  public void initializeDefaultPreferences()
  {
    IPreferenceStore store = ConnectorNodePlugin.getDefault().getPreferenceStore();
    
    store.setDefault("apiID", "");
    store.setDefault("apiKey", "");
    store.setDefault("swaggerFile", "");
    
  }
  
  
}
