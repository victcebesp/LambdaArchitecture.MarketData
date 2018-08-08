package org.eifer.box.ness;

import io.intino.konos.datalake.Datalake;
import io.intino.tara.magritte.Graph;
import org.eifer.box.MounterBox;
import projection.Report;
import projection.ReportExporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class ReflowAssistant {

	private final MounterBox box;

	ReflowAssistant(MounterBox box) {
		this.box = box;
	}

	void before() {

	}

	int defaultBlockSize() {
		return Integer.MAX_VALUE;
	}

	List<Datalake.Tank> defaultTanks() {
	 	return java.util.Collections.emptyList(); //TODO
	}

	Graph graph() {
//		return null;
		return box.graph().core$();
	}

	String[] coreStashes() {
		return new String[]{"Model"};
	}

	void saveGraph(Graph graph) {
		graph.saveAll("Model");
	}

	void after() {

		String path = box.configuration().get("csv_store");
		String csvHeading = "price zone;date;from;to;type;day-ahead price;day-ahead volume;continuous weighted volume;continuous ID3\n";
		StringBuilder germanyAustriaCSV = new StringBuilder(csvHeading);
		StringBuilder switzerlandCSV = new StringBuilder(csvHeading);
		StringBuilder franceCSV = new StringBuilder(csvHeading);

		germanyAustriaCSV.append(ReportExporter.mapReportStreamToString(MounterBox.reports.entrySet().stream()
				.filter(e -> e.getValue().priceZone().equals("DE-AT"))));

		switzerlandCSV.append(ReportExporter.mapReportStreamToString(MounterBox.reports.entrySet().stream()
				.filter(e -> e.getValue().priceZone().equals("CH"))));

		franceCSV.append(ReportExporter.mapReportStreamToString(MounterBox.reports.entrySet().stream()
				.filter(e -> e.getValue().priceZone().equals("FR"))));

		try {
			new File(path).mkdirs();
			Files.write(Paths.get(path + "/DE-AT_view.csv"), germanyAustriaCSV.toString().getBytes());
			Files.write(Paths.get(path + "/CH_view.csv"), switzerlandCSV.toString().getBytes());
			Files.write(Paths.get(path + "/FR_view.csv"), franceCSV.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}