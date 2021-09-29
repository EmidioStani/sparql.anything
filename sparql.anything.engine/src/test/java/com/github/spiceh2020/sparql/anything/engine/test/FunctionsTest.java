/*
 * Copyright (c) 2021 Enrico Daga @ http://www.enridaga.net
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.spiceh2020.sparql.anything.engine.test;

import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.main.QC;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.github.spiceh2020.sparql.anything.engine.FacadeX;

public class FunctionsTest {

	public ResultSet execute (String queryString){

		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		Dataset kb = DatasetFactory.createGeneral();
		Query q = QueryFactory
				.create(queryString);
		ResultSet result = QueryExecutionFactory.create(q, kb).execSelect();
		return result;
	}

	@Test
	public void next(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?three WHERE {" +
					"BIND(fx:next(rdf:_2) as ?three)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String threeUri = result.next().get("three").asResource().getURI();
		Assert.assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#_3", threeUri);
	}

	@Test
	public void previous(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?three WHERE {" +
				"BIND(fx:previous(rdf:_4) as ?three)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String threeUri = result.next().get("three").asResource().getURI();
		Assert.assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#_3", threeUri);
	}

	@Test
	public void after(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?true WHERE {" +
				"BIND(fx:after(rdf:_4, rdf:_3) as ?true)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String trueStr = result.next().get("true").asNode().toString();
		Assert.assertEquals("\"true\"^^http://www.w3.org/2001/XMLSchema#boolean", trueStr);
	}

	@Test
	public void before(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?true WHERE {" +
				"BIND(fx:before(rdf:_2, rdf:_3) as ?true)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String trueStr = result.next().get("true").asNode().toString();
		Assert.assertEquals("\"true\"^^http://www.w3.org/2001/XMLSchema#boolean", trueStr);
	}

	@Test
	public void forward(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?seven WHERE {" +
				"BIND(fx:forward(rdf:_2, 5) as ?seven)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String sevenUri = result.next().get("seven").asResource().getURI();
		Assert.assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#_7", sevenUri);
	}

	@Test
	public void backward(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?twenty WHERE {" +
				"BIND(fx:backward(rdf:_24, 4) as ?twenty)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String twentyUri = result.next().get("twenty").asResource().getURI();
		Assert.assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#_20", twentyUri);
	}

	@Test
	public void substring1(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?ob WHERE {" +
				"BIND(fx:String.substring(\"bob\", 1) as ?ob)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String ob = result.next().get("ob").asLiteral().getString();
		Assert.assertEquals("ob", ob);
	}

	@Test
	public void substring2(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?bo WHERE {" +
				"BIND(fx:String.substring(\"bob\", 0, 2) as ?bo)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String bo = result.next().get("bo").asLiteral().getString();
		Assert.assertEquals("bo", bo);
	}

	@Test
	public void trim(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?bob WHERE {" +
				"BIND(fx:String.trim(\" bob \") as ?bob)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		String bob = result.next().get("bob").asLiteral().getString();
		Assert.assertEquals("bob", bob);
	}

	@Test
	@Ignore
	public void indexOf(){
		String q = "PREFIX fx:  <http://sparql.xyz/facade-x/ns/>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"SELECT ?one WHERE {" +
				"BIND(fx:String.indexOf(\"bob\", \"o\") as ?one)" +
				"}";
		ResultSet result = execute(q);
		Assert.assertTrue(result.hasNext());
		int one = result.next().get("one").asLiteral().getInt();
		Assert.assertEquals(1, one);
	}
}