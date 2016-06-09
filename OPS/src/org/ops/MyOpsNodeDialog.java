package org.ops;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;

import net.sf.json.JSONArray;

/**
 * <code>NodeDialog</code> for the "MyOps" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class MyOpsNodeDialog extends NodeDialogPane {

  	
	protected final SettingsModelString templateSelection = new SettingsModelString(
			MyOpsNodeModel.TEMPLATE_SELECTION,
			null);

	private List<String> tempParams = new ArrayList<String>();
	private Map<String, String> params = new HashMap<String, String>();

	protected final SettingsModelString resultUrl = new SettingsModelString(
			MyOpsNodeModel.RESULT_URL, MyOpsNodeModel.RESULT_URL_DEFAULT);

    JPanel frame = new JPanel(new BorderLayout());
        
    JComboBox<String> options;
    
    /**
     * New pane for configuring MyOps node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected MyOpsNodeDialog() {
        super();
        

      	Swagger swagger = new SwaggerParser().read("https://dev.openphacts.org/swagger/spec/ops_2_1.json");			 
     	  /***** ALL PATHS *******/
        List<String> callNames = new ArrayList<String>();

        Map<String, Path>	paths =  swagger.getPaths();
    	  paths.forEach((k, v) -> {
    		  
    		  callNames.add(v.getOperations().get(0).getSummary().replace(":", "")+" : "+k.toString());

    	  	}
    	  
    	   );
    	  

 		String[] stockArr = new String[callNames.size()];
 		stockArr = (String[]) callNames.toArray(stockArr);

 		DialogComponentStringSelection templateDialog  = new DialogComponentStringSelection(templateSelection,
 				"Select service", stockArr);

	    options = new JComboBox<>(stockArr);
 		
 		options.addActionListener(
 				
 				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
	 			    	   
						String name = String.valueOf(options.getSelectedItem());
						name = name.substring(name.indexOf(":")+2);
		 			   	System.out.println(name);
		 			   	
		 				String[] stock = new String[callNames.size()];
		 				stock = (String[]) callNames.toArray(stock);

		 				getSingleMethod(swagger, name);       		
		 				
		 				Component temp = createTab(swagger, name);
		 				frame.removeAll();
		 		 		frame.add(options, BorderLayout.NORTH);
		 				frame.add(temp, BorderLayout.SOUTH);
		 				
		 				frame.revalidate();
		 				frame.repaint();

					}
 				}
 				
 		);    	
 	
 		frame.add(options, BorderLayout.NORTH);

 		addTab("Options",frame); 	 				


      }
      
      public static void getSingleMethod( Swagger swagger, String name){
      	
     	 List<Parameter> parameters = swagger.getPaths().get(name).getGet().getParameters();
   		  for (Parameter single : parameters){
   	     	 io.swagger.models.parameters.QueryParameter query;

   			  if(single.getClass().toString().indexOf("io.swagger.models.parameters.QueryParameter")!=-1){
   				    query = (io.swagger.models.parameters.QueryParameter) single;
          			 System.out.println(query.getName()+" * "+((query.getEnum()==null)?"":query.getEnum())+" "+query.getDefaultValue()+" "+query.getRequired());       	  	
   			  }else
       			 System.out.println(single.getName());       	  	
   		  } 
   	}
      
      private Component createTab(Swagger swagger, String name)
      {	          
    	  JPanel connectionPanel = new JPanel(new GridBagLayout());
          List<Parameter> parameters = swagger.getPaths().get(name).getGet().getParameters();
    		  for (Parameter single : parameters){
    	     	 io.swagger.models.parameters.QueryParameter query;

    			  if(single.getClass().toString().indexOf("io.swagger.models.parameters.QueryParameter")!=-1){
    				    
    				  query = (io.swagger.models.parameters.QueryParameter) single;
    				  final JTextField field = new JTextField(20);
    				  field.setToolTipText(query.getName()+" : "+query.getDescription());     				
    				  
    				  if(query.getEnum()!=null){
        			 	  JComboBox<String> options = new JComboBox<>(query.getEnum().toArray(new String[query.getEnum().size()]));
        			 	  addField(connectionPanel, query.getName(), options);
    				  }else{
        			 	  addField(connectionPanel, query.getName(), field);    					  
    				  }
    				  
    				  
    			  }else{
        			 System.out.println(single.getName());  
      				 final JTextField field = new JTextField(20);
     				 field.setToolTipText(single.getName());
     				 addField(connectionPanel, single.getName(),  field);   				  
    			  }
 				

    		  }
    		  
    	        JScrollPane scrollPane = new JScrollPane(connectionPanel);
    	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	           	       
			return scrollPane; 
          

      }
      
      protected <T extends JComponent> T addField(JPanel panel, String label, T field)
      {
          panel.add(new JLabel(label ), createFirst());
          panel.add(field, createLast());
          return field;
      }
      
      protected GridBagConstraints createLast()
      {
          GridBagConstraints last = createFirst();
          last.gridwidth = GridBagConstraints.REMAINDER;
          return last;
      }

      protected GridBagConstraints createFirst()
      {
          GridBagConstraints constraints = new GridBagConstraints();
          constraints.anchor = GridBagConstraints.WEST;
          constraints.fill = GridBagConstraints.HORIZONTAL;
          constraints.insets = new Insets(2, 3, 2, 3);
          return constraints;
      }
      


	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

		tempParams= new ArrayList<String>();
		String name = String.valueOf(options.getSelectedItem());
		name = name.substring(name.indexOf(":")+2);
		params.put("name", name);
		
		JScrollPane scroll = (JScrollPane) frame.getComponents()[1];
		JViewport port = (JViewport) scroll.getComponent(0);
	    JPanel panel = (JPanel) port.getComponent(0);
		    String keyName=""; String value="";
			int i=0;
	        while(i < panel.getComponents().length) {
	           System.out.println(panel.getComponents()[i].toString());
	           System.out.println(panel.getComponents()[i].getClass().toString());	           

	           if(panel.getComponents()[i].getClass().toString().indexOf("JLabel")!=-1)
	           {
	        	   JLabel label= (JLabel) panel.getComponents()[i];
	        	   keyName=label.getText();
	        	   System.out.println(keyName);
	           }

	           if(panel.getComponents()[i].getClass().toString().indexOf("TextField")!=-1)
	           {
	        	   JTextField field = (JTextField) panel.getComponents()[i];
	        	   value=field.getText();
	        	   System.out.println(value);
	        	   tempParams.add(value);
	        	   params.put(keyName, value);
	           }

	           if(panel.getComponents()[i].getClass().toString().indexOf("Box")!=-1)
	           {
	        	   JComboBox box = (JComboBox) panel.getComponents()[i];
	        	   value=box.getSelectedItem().toString();
	        	   System.out.println(value);
	        	   tempParams.add(value);
	        	   params.put(keyName, value);
	           }

	           i++;
	         }

	        Gson gson = new Gson();
	        System.out.println(gson.toJson(params));


			settings.addString("resultUrl", gson.toJson(params));

	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {

		try {
			resultUrl.setStringValue(settings.getString("resurltUrl"));
			
		} catch (InvalidSettingsException e) {
			// e.printStackTrace();
			System.out.println("Settings missing in load method");

		}
	}
}

