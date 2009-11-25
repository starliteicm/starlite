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
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.Money;

import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def crew = manager.getAllCrew()
	pageContext["title"] = "Crew Payments"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	println period
	
	def report = []
    def dueTotal = 0
    def paidTotal = 0
    def varTotal = 0
	for (CrewMember cm in crew) {
    	def reportRow = [:];
        reportRow["lastName"] = cm.getPersonal().getLastName();
        reportRow["firstName"] = cm.getPersonal().getFirstName();
        reportRow["bankName"] = cm.getBanking().getBankName();
        reportRow["branchCode"] =     cm.getBanking().getBranchCode();
        reportRow["accountName"] =    cm.getBanking().getAccountName();
        reportRow["accountNumber"] =  cm.getBanking().getAccountNumber();
        reportRow["address1"] =       cm.getBanking().getAddress1();
        reportRow["address2"] =       cm.getBanking().getAddress2();
        reportRow["address3"] =       cm.getBanking().getAddress3();
        reportRow["address4"] =       cm.getBanking().getAddress4();
        reportRow["address5"] =       cm.getBanking().getAddress5();
        reportRow["swift"] =          cm.getBanking().getSwift();
        reportRow["iban"] =           cm.getBanking().getIban();
        reportRow["bic"] =            cm.getBanking().getBic();
        
        CrewMember.FlightAndDutyActuals actuals = cm.getFlightAndDutyActualsForMonth(period);
        def total = actuals.getTotal()
        reportRow["total"] = total
        def paidAmount = actuals.getPaidAmount()
        if (paidAmount == null)
        	paidAmount = new Money(total.getCurrency(), 0)
        else
        	paidAmount.setCurrencyCode(total.getCurrencyCode())
        reportRow["paid"] = paidAmount
        
        def variance = paidAmount.subtract(total)
        
        reportRow["variance"] = variance
        
        reportRow["now"] = new Date()
        
        dueTotal += total.getAmount()
        paidTotal += paidAmount.getAmount()
        varTotal += variance.getAmount()
        
        report.add(reportRow)
    }
    
    def totalRow =[:];
    totalRow["lastName"] = "zz Total"
    totalRow["firstName"] = ""
    totalRow["total"]  = new Money("USD", dueTotal)
    totalRow["variance"] = new Money("USD", varTotal)
    totalRow["paid"] = new Money("USD", paidTotal)
    
    report.add(totalRow)
	
	def datePeriod = new org.joda.time.DateMidnight(Integer.valueOf(year), Integer.valueOf(month), 1).toDate()
	def dateFormat = new java.text.SimpleDateFormat("MMM yy")
	
	return Table.create("payments", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Payments " + dateFormat.format(datePeriod))
		.withColumns()
			.column("lastName").sort(0)
			.column("firstName").sort(1)
			.column("bankName").called("Bank")
			.column("branchCode")  
			.column("accountName") 
			.column("accountNumber")
			.column("address1")    
			.column("address2")    
			.column("address3")    
			.column("address4")    
			.column("address5")    
			.column("swift")       
			.column("iban")        
			.column("bic")    
			.column("total").called("Total Due").withStyle("text-align:right;width:80px;")
			.column("paid").called("Amount Paid").withStyle("text-align:right;width:80px;")
			.column("variance").withStyle("text-align:right;width:80px;")
		.render();
}
