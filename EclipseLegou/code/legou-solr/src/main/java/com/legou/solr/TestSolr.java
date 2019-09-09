package com.legou.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {
	
	@Test
	public void testAdd() throws SolrServerException, IOException {
		SolrServer server = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("id", "001");
		solrInputDocument.addField("item_title", "hhha");
		solrInputDocument.addField("item_sell_point", "niubi");
		solrInputDocument.addField("item_price", 1000);
		
		SolrInputDocument solrInputDocument2 = new SolrInputDocument();
		solrInputDocument2.addField("id", "123456");
		solrInputDocument2.addField("item_title", "hhhe");
		solrInputDocument2.addField("item_sell_point", "niubia");
		solrInputDocument2.addField("item_price", 200);
		
		server.add(solrInputDocument);
		server.add(solrInputDocument2);
		server.commit();
	}
	
	@Test
	public void testDel() throws SolrServerException, IOException {
		SolrServer server = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		
		server.deleteById("001");
		server.commit();
	}
	
	@Test
	public void testQuery() throws SolrServerException {
		SolrServer server = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
		
		SolrQuery solrQuery = new SolrQuery();
		
		solrQuery.set("q","*:*");
		
		QueryResponse query = server.query(solrQuery);
		
		SolrDocumentList results = query.getResults();
		
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
}
