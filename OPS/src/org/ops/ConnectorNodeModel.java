package org.ops;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.json.JSONCellFactory;
import org.knime.core.data.xml.XMLCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of MyOps.
 * 
 *
 * @author 
 */
public class ConnectorNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(ConnectorNodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */


	public static final String RESULT_URL = "resultUrl";

	public static final String RESULT_URL_DEFAULT = "S";
    	
    protected final SettingsModelString resultUrl = new SettingsModelString(ConnectorNodeModel.RESULT_URL,ConnectorNodeModel.RESULT_URL_DEFAULT);

    /**
     * Constructor for the node model.
     */
    protected ConnectorNodeModel() {
    
        // TODO one incoming port and two outgoing port is assumed
        super(1, 2);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	// String template
    	DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = 
                new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);

        // Other Output
    	DataColumnSpec[] responseSpecs = new DataColumnSpec[1];
    	responseSpecs[0] = 
                new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        DataTableSpec responseSpec = new DataTableSpec(responseSpecs);

        // JSON template
        
    	DataColumnSpec[] jsonColSpecs = new DataColumnSpec[1];
        jsonColSpecs[0] = 
                new DataColumnSpecCreator("url", JSONCellFactory.TYPE).createSpec();
        DataTableSpec jsonOutputSpec = new DataTableSpec(jsonColSpecs);

        // XML Template

    	DataColumnSpec[] xmlColSpecs = new DataColumnSpec[1];
        xmlColSpecs[0] = 
                new DataColumnSpecCreator("url", XMLCellFactory.TYPE).createSpec();
        DataTableSpec xmlOutputSpec = new DataTableSpec(xmlColSpecs);

        
        // String Container
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        DataCell[] cells = new DataCell[1];

        BufferedDataContainer containerOutput = exec.createDataContainer(responseSpec);
        DataCell[] cellsOutput = new DataCell[1];

        // JSON Container
        BufferedDataContainer jsonContainer = exec.createDataContainer(jsonOutputSpec);
        DataCell[] jsonCells = new DataCell[1];
        
        // XML Container
        BufferedDataContainer xmlContainer = exec.createDataContainer(xmlOutputSpec);
        DataCell[] xmlCells = new DataCell[1];

        Iterator<DataRow> varIt = inData[0].iterator();
	    
        String uriVal="";
    	if(varIt.hasNext()){    			
    			DataRow current = varIt.next();
    			uriVal=current.getCell(0).toString(); 
    			System.out.println(uriVal);
    	}
    	
    	
        System.out.println("Result Url: "+resultUrl.getStringValue());
        
        IPreferenceStore store = ConnectorNodePlugin.getDefault().getPreferenceStore();
        System.out.println("Model Pref Value:"+store.getString("apiKey")+" "+store.getString("apiID"));

        String format="";
        JsonObject jobj= new JsonObject();
        
        if(resultUrl.getStringValue().length()>2){
	     
		  jobj = new Gson().fromJson(resultUrl.getStringValue(), JsonObject.class);
    	  format = jobj.get("_format").getAsString();
	  }

        List jsonKeys = getKeysFromJson(resultUrl.getStringValue());
        
        String url="https://beta.openphacts.org/2.1"+jobj.get("name").getAsString()+"?";
        String tempKey="";
        
  		int i = 0;
  		while (i < jsonKeys.size()) {
  			
  			tempKey=jsonKeys.get(i).toString();

  			if((!tempKey.equals("name")&&!tempKey.equals("_callback") && !tempKey.equals("_metadata"))& ( jobj.get(jsonKeys.get(i).toString()).getAsString().length()>0 || tempKey.equals("uri") ) ){	    	  				
  				
  	  				if(tempKey.equals("app_key"))
  	  					url=url+jsonKeys.get(i).toString()+"="+((store.getString("apiKey").isEmpty())?jobj.get(jsonKeys.get(i).toString()).getAsString():store.getString("apiKey"))+"&";
  	  				else if(tempKey.equals("app_id"))
  	  					url=url+jsonKeys.get(i).toString()+"="+((store.getString("apiID").isEmpty())?jobj.get(jsonKeys.get(i).toString()).getAsString():store.getString("apiID"))+"&";
  	  				else if (tempKey.equals("uri")){
  	  					if(jsonKeys.get(i).toString().equals("uri"))
  	  						url=url+jsonKeys.get(i).toString()+"="+URLEncoder.encode(uriVal,"UTF-8")+"&";  	  						  	  						
  	  				}
  	  				else
  	  					url=url+jsonKeys.get(i).toString()+"="+jobj.get(jsonKeys.get(i).toString()).getAsString()+"&";
  	    	
  			}
  			i++;
  			
  		}
        
        
	  cells[0] = new StringCell(resultUrl.getStringValue());	  
	  DataRow row = new DefaultRow("parameters", cells);
	  DataRow row2 = new DefaultRow("url", url);

      container.addRowToTable(row);
      container.addRowToTable(row2);

      container.close();
      
      BufferedDataTable out = container.getTable();      
	  
	  if(format.equals("json"))
	  {
		  jsonCells[0] = (DataCell) JSONCellFactory.create(httpGet(url), true);		  
		  DataRow jsonrow = new DefaultRow("JSON", jsonCells);
	      jsonContainer.addRowToTable(jsonrow);
	      jsonContainer.close();
	      BufferedDataTable jsonOuput = jsonContainer.getTable();	      
	      
	      return new BufferedDataTable [] {jsonOuput, out};
	       
	  }
	  else if ( format.equals("xml")){
		  
	       xmlCells[0] = (DataCell) XMLCellFactory.create(httpGet(url));
	       DataRow xmlrow = new DefaultRow("XML", xmlCells);
	       xmlContainer.addRowToTable(xmlrow);
	       xmlContainer.close();
	       BufferedDataTable xmlOuput = xmlContainer.getTable();
	       return new BufferedDataTable [] {xmlOuput, out};

	  }else{
		  
		  cellsOutput[0] = new StringCell(httpGet(url));	  
		  DataRow rowOutput = new DefaultRow("Response", cellsOutput);
	      containerOutput.addRowToTable(rowOutput);
	      containerOutput.close();
	      BufferedDataTable outResponse = containerOutput.getTable();
	      return new BufferedDataTable [] {outResponse, out};
		  
	  }

    }
    
    
    public static String httpGet(String urlStr) throws IOException {
        	
  	  URL url = new URL(urlStr);
  	  HttpURLConnection conn =
  	      (HttpURLConnection) url.openConnection();

  	  if (conn.getResponseCode() != 200) {
  		  
  		  switch(conn.getResponseCode()){
  		  
  		  case 403 :  return "{\""+conn.getResponseCode()+"\": \" authorization failed \"}";
  		  case 400 :  return "{\""+conn.getResponseCode()+"\": \" request was invalid \"}";
  		  case 404 :  return "{\""+conn.getResponseCode()+"\": \" nothing could be found \"}";
  		  case 500 :  return "{\""+conn.getResponseCode()+"\": \" internal server error occurred \"}";
  		  
  		  }
  	  

  	  }

  	  // Buffer the result into a string
  	  BufferedReader rd = new BufferedReader(
  	      new InputStreamReader(conn.getInputStream()));
  	  StringBuilder sb = new StringBuilder();
  	  String line;
  	  while ((line = rd.readLine()) != null) {
  		  System.out.println(line);
  	    sb.append(line);
  	  }
  	  rd.close();

  	  conn.disconnect();
  	  return sb.toString();
  	}


    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return new DataTableSpec[]{null, null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        // TODO save user settings to the config object.
        resultUrl.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.
        resultUrl.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO check if the settings could be applied to our model
        // e.g. if the count is in a certain range (which is ensured by the
        // SettingsModel).
        // Do not actually set any values of any member variables.
        resultUrl.validateSettings(settings);
        

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }
    
    static List getKeysFromJson(String fileName) throws Exception
    {
      Object things = new Gson().fromJson(fileName, Object.class);
      List keys = new ArrayList();
      collectAllTheKeys(keys, things);
      return keys;
    }

    static void collectAllTheKeys(List keys, Object o)
    {
      Collection values = null;
      if (o instanceof Map)
      {
        Map map = (Map) o;
        keys.addAll(map.keySet()); // collect keys at current level in hierarchy
        values = map.values();
      }
      else if (o instanceof Collection)
        values = (Collection) o;
      else // nothing further to collect keys from
        return;

      for (Object value : values)
        collectAllTheKeys(keys, value);
    }



}

