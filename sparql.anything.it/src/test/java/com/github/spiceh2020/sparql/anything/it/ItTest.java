package com.github.spiceh2020.sparql.anything.it;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.spiceh2020.sparql.anything.engine.FacadeX;
import com.github.spiceh2020.sparql.anything.model.Triplifier;

public class ItTest {
	private static final Logger log = LoggerFactory.getLogger(ItTest.class);

	@Test
	public void RegistryExtensionsTest() {
		for (String ext : new String[] { "json", "html", "xml", "csv", "bin", "png", "jpeg", "jpg", "bmp", "tiff",
				"tif", "ico", "txt", "xslx", "xls" }) {
			Assert.assertNotNull(ext, FacadeX.Registry.getTriplifierForExtension(ext));
		}
	}

	@Test
	public void RegistryMimeTypesTest() {
		for (String mt : new String[] { "application/json", "text/html", "application/xml", "text/csv",
				"application/octet-stream", "image/png", "image/jpeg", "image/bmp", "image/tiff",
				"image/vnd.microsoft.icon", "application/vnd.ms-excel",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }) {
			Assert.assertNotNull(mt, FacadeX.Registry.getTriplifierForMimeType(mt));
		}
	}

	@Test
	public void CSV1() throws URISyntaxException {
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		String location = getClass().getClassLoader().getResource("test1.csv").toURI().toString();
		log.info("{}", location);
		Query query = QueryFactory.create("SELECT distinct ?p WHERE { SERVICE <x-sparql-anything:location=" + location
				+ "> { ?s ?p ?o }} ORDER BY ?p");
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		Map<Integer, String> expected = new HashMap<Integer, String>();
		expected.put(1, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_1");
		expected.put(2, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_2");
		expected.put(3, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_3");
		expected.put(4, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_4");
		expected.put(5, RDF.type.getURI());
		while (rs.hasNext()) {
			int rowId = rs.getRowNumber() + 1;
			QuerySolution qs = rs.next();
			log.trace("{} {} {}", rowId, qs.get("p").toString(), expected.get(rowId));
			Assert.assertTrue(expected.get(rowId).equals(qs.get("p").toString()));
		}
	}

	@Test
	public void CSV1headers() throws URISyntaxException {
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		String location = getClass().getClassLoader().getResource("test1.csv").toURI().toString();
		log.info("{}", location);
		Query query = QueryFactory.create(
				"SELECT distinct ?p WHERE { SERVICE <x-sparql-anything:csv.headers=true,namespace=http://www.example.org/csv#,location="
						+ location + "> { ?s ?p ?o }} ORDER BY ?p");
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		Map<Integer, String> expected = new HashMap<Integer, String>();

		expected.put(1, "http://www.example.org/csv#A");
		expected.put(2, "http://www.example.org/csv#B");
		expected.put(3, "http://www.example.org/csv#C");
		expected.put(4, "http://www.example.org/csv#D");
		expected.put(5, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_1");
		expected.put(6, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_2");
		expected.put(7, RDF.type.getURI());
		while (rs.hasNext()) {
			int rowId = rs.getRowNumber() + 1;
			QuerySolution qs = rs.next();
			log.trace("{} {} {}", rowId, qs.get("p").toString(), expected.get(rowId));
			Assert.assertTrue(expected.get(rowId).equals(qs.get("p").toString()));
		}
	}

	@Test
	public void CSV1headersDefaultNS() throws URISyntaxException {
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		String location = getClass().getClassLoader().getResource("test1.csv").toURI().toString();
		log.info("{}", location);
		Query query = QueryFactory
				.create("SELECT distinct ?p WHERE { SERVICE <x-sparql-anything:csv.headers=true,location=" + location
						+ "> { ?s ?p ?o }} ORDER BY ?p");
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
//		System.out.println(ResultSetFormatter.asText(rs));
		Map<Integer, String> expected = new HashMap<Integer, String>();

		expected.put(1, "http://sparql.xyz/facade-x/data/A");
		expected.put(2, "http://sparql.xyz/facade-x/data/B");
		expected.put(3, "http://sparql.xyz/facade-x/data/C");
		expected.put(4, "http://sparql.xyz/facade-x/data/D");
		expected.put(5, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_1");
		expected.put(6, "http://www.w3.org/1999/02/22-rdf-syntax-ns#_2");
		expected.put(7, RDF.type.getURI());

		while (rs.hasNext()) {
			int rowId = rs.getRowNumber() + 1;
			QuerySolution qs = rs.next();
//			System.out.println(rowId+" "+qs.get("p").toString()+" "+expected.get(rowId));
			log.trace("{} {} {}", rowId, qs.get("p").toString(), expected.get(rowId));
			Assert.assertTrue(expected.get(rowId).equals(qs.get("p").toString()));
		}
	}

	@Test
	public void JSON1() throws URISyntaxException {
		// a01009-14709.json
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		String location = getClass().getClassLoader().getResource("tate-gallery/a01009-14709.json").toURI().toString();
		Query query = QueryFactory.create(
				"SELECT DISTINCT ?p WHERE { SERVICE <x-sparql-anything:namespace=http://www.example.org#,location="
						+ location + "> { ?s ?p ?o }} order by ?p");
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		List<String> mustInclude = new ArrayList<String>(
				Arrays.asList(new String[] { "http://www.example.org#thumbnailUrl", "http://www.example.org#title",
						"http://www.w3.org/1999/02/22-rdf-syntax-ns#_1", "http://www.example.org#text",
						"http://www.example.org#subjects", "http://www.example.org#subjectCount" }));
		while (rs.hasNext()) {
			int rowId = rs.getRowNumber() + 1;
			QuerySolution qs = rs.next();
			log.trace("{} {}", rowId, qs.get("p").toString());
			mustInclude.remove(qs.get("p").toString());
		}
		Assert.assertTrue(mustInclude.isEmpty());
	}

	@Test
	public void JPG1() throws URISyntaxException {
		// A01009_8.jpg
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		String location = getClass().getClassLoader().getResource("tate-gallery/A01009_8.jpg").toURI().toString();
		Query query = QueryFactory
				.create("SELECT * WHERE { SERVICE <x-sparql-anything:location=" + location + "> { ?s ?p ?o }}");
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		// Should only contain 1 triple
		while (rs.hasNext()) {
//            int rowId = rs.getRowNumber() + 1;
			QuerySolution qs = rs.next();
			Assert.assertTrue(qs.get("s").asNode().isBlank());
			Assert.assertTrue(qs.get("p").toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#_1")
					|| qs.get("p").toString().equals(RDF.type.getURI()));
			Assert.assertTrue(qs.get("o").isLiteral() || qs.get("o").toString().equals(Triplifier.FACADE_X_TYPE_ROOT));
		}
	}

	@Test
	public void NestedTest1() throws URISyntaxException {
		String location = getClass().getClassLoader().getResource("tate-gallery/artwork_data.csv").toURI().toString();
		String queryStr = "" + "prefix ex: <http://www.example.org#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "SELECT ?accession ?thumbnail ?image {"
				+ "BIND (IRI(CONCAT(\"x-sparql-anything:\", ?thumbnail )) AS ?embed ) . "
				+ "SERVICE <x-sparql-anything:csv.headers=true,namespace=http://www.example.org#,location=" + location
				+ "> {" + "FILTER (?accession = \"A01009\") . "
				+ "[] ex:accession ?accession ; ex:thumbnailUrl ?thumbnail " + "}" + ""
				+ "SERVICE ?embed { [] rdf:_1 ?image } . " + "} LIMIT 1";
		log.info("\n{}\n", queryStr);

		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		Query query = QueryFactory.create(queryStr);
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			Assert.assertTrue(qs.get("accession").isLiteral());
			Assert.assertTrue(qs.get("thumbnail").isLiteral());
			Assert.assertTrue(qs.get("image").isLiteral());
			Assert.assertTrue(qs.get("image").asNode().getLiteralDatatypeURI()
					.equals("http://www.w3.org/2001/XMLSchema#base64Binary"));
		}
	}

	@Test
	public void TriplifyTateGalleryArtworkData() throws IOException, URISyntaxException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("tate-gallery1.sparql");
		String location = getClass().getClassLoader().getResource("tate-gallery/artwork_data.csv").toURI().toString();
		String queryStr = IOUtils.toString(is, "UTF-8").replace("%%artwork_data%%", location);
		log.info(queryStr);
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		Query query = QueryFactory.create(queryStr);
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			log.info("{}}", qs);
		}
//        Model model = QueryExecutionFactory.create(query, kb).execConstruct();
//        log.info("Produced {} triples", model.size());
//        // Write as Turtle via model.write
//        model.write(System.out, "TTL") ;
	}

	@Test
	public void TriplifyTateGalleryArtworkJSON() throws IOException, URISyntaxException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("tate-gallery2.sparql");
		String location = getClass().getClassLoader().getResource("tate-gallery/a01003-14703.json").toURI().toString();
		String queryStr = IOUtils.toString(is, "UTF-8").replace("%%artwork_json%%", location);
		log.info(queryStr);
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		Query query = QueryFactory.create(queryStr);
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			log.info("{}}", qs);
		}
//        Model model = QueryExecutionFactory.create(query, kb).execConstruct();
//        log.info("Produced {} triples", model.size());
//        // Write as Turtle via model.write
//        model.write(System.out, "TTL") ;
	}

	@Test
	public void triplifySpreadsheet() throws IOException, URISyntaxException {
		String location = getClass().getClassLoader().getResource("Book1.xls").toURI().toString();
		Query query = QueryFactory.create("SELECT distinct ?p WHERE { SERVICE <x-sparql-anything:location=" + location
				+ "> { ?s ?p ?o }} ORDER BY ?p");
		Dataset kb = DatasetFactory.createGeneral();
		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
		Assert.assertTrue(rs.hasNext());
	}
}
