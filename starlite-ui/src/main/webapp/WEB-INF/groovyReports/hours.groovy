import org.jmesa.limit.Limit;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlTable;

import com.itao.starlite.ui.jmesa.ExpirableDateEditor;

import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def crew = manager.getAllCrew()
	pageContext["title"] = "Crew Hours Report"
	
	def report = []
    pageContext["title"] = "Hours"
    
    for (cm in crew) {
      def reportRow = [:];
      if(cm.personal.lastName != null){
      if(!"".equals(cm.personal.lastName)){
      reportRow["firstName"]    = cm.personal.firstName
      reportRow["lastName"]     = cm.personal.lastName
      if(cm.role.totalHours != null){
        reportRow["total"]        = cm.role.totalHours
      }
      else{
        reportRow["total"]        = new Double(0.0)
      }
      reportRow["Total PIC"]    = new Double(0.0)
      reportRow["Total Turbine"]= new Double(0.0)
      reportRow["NVG"]          = new Double(0.0)
      reportRow["Offshore"]     = new Double(0.0)
      reportRow["Undersling"]   = new Double(0.0)
      reportRow["S330J"]        = new Double(0.0)
      reportRow["S92"]          = new Double(0.0)
      
      System.out.println(cm.role.getConversions())
      
      for(conv in cm.role.conversions) {
        //Total PIC
        if(conv.quantity != null){
        
        if("Total PIC".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)} 
        //Total Turbine
        else if("Total Turbine".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)}
        //NVG
        else if("NVG".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)}
        //Offshore
        else if("Offshore".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)}
        //Undersling
        else if("Undersling".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)}
        //S330J
        else if("S330J".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)}
        //S92
        else if("S92".equals(conv.number)){reportRow[conv.number]=new Double(conv.quantity)}
        
        }
      }
      
      report.add(reportRow)
      }
    }
    }
	
	return Table.create("hours", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Hours")
		.withColumns()
			.column("firstName").called("First Name")
			.column("lastName").called("Last Name")
			.column("Total PIC").called("Total PIC")
			.column("Total Turbine").called("Total Turbine")
			.column("NVG").called("NVG")
			.column("Offshore").called("Offshore")
			.column("Undersling").called("Undersling")
			.column("S330J").called("S330J")
			.column("S92").called("S92")
			.column("total").called("total")
		.render();
}
