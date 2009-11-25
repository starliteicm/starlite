package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.joda.time.DateMidnight;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.itao.persistence.WarpPersistGuiceModuleConfig;
import com.itao.starlite.dao.ActualsDao;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.Charter;


public class ProcessFile {
	
	public static void main(String[] args) throws IOException {
		Module m = new WarpPersistGuiceModuleConfig();
		Injector i = Guice.createInjector(m);
		StarliteCoreManager manager = i.getInstance(StarliteCoreManager.class);
		
		Charter c = manager.getCharter(7);
		Aircraft air = manager.getAircraft(5);
		
		BufferedReader r = new BufferedReader(new FileReader("c:/users/admin/desktop/starlite-actuals.csv"));
		java.io.File f = new File("c:/users/admin/desktop/output.csv");
		f.createNewFile();
		java.io.Writer w = new FileWriter(f);
		ActualsDao dao = i.getInstance(ActualsDao.class);
		
		String l = r.readLine();
		
		while (l != null) {
			String[] parts = l.split(",");
			String date = parts[6];
			//String newDate = date.substring(6) + "-" + date.substring(3, 5) +"-" + date.substring(0,2);
			int year = Integer.parseInt(date.substring(6));
			int month = Integer.parseInt(date.substring(3,5));
			int day = Integer.parseInt(date.substring(0,2));
			if (parts[1] != null && parts[1].equals("PDOC")) {
				//w.write(newDate+","+parts[1]+","+parts[2]+","+parts[3]+","+parts[4]+","+parts[5]+"\n");
				Actuals a = new Actuals();
				a.setCharter(c);
				//String[] dateParts = newDate.split("-");
				a.setAircraft(air);
				DateMidnight dm = new DateMidnight(year, month, day);
				a.setDate(dm.toDate());
				if (parts[2] != null && !parts[2].equals(""))
					a.setCapt(Double.parseDouble(parts[2]));
				if (parts[3] != null && !parts[3].equals(""))
					a.setAframe(Double.parseDouble(parts[3]));
				if (parts[4] != null && !parts[4].equals(""))
					a.setLandings(Integer.parseInt(parts[4]));
				if (parts[5] != null && !parts[5].equals(""))
					a.setPax(Integer.parseInt(parts[5]));
				dao.makePersistent(a);
			}
			l = r.readLine();
		}
		w.flush();
		r.close();
		w.close();
	}
}
