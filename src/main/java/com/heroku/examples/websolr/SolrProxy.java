package com.heroku.examples.websolr;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrProxy extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4562420242671959061L;
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(SolrProxy.class);
			  System.out.println(String.format(">>>>>>>>>>>>>>>>Using SOLR Index Url : %s",System.getenv("SOLR_INDEX_URL") ));
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getParameter("stats")!=null && req.getParameter("stats").equalsIgnoreCase("true")){
			  CommonsHttpSolrServer server = new CommonsHttpSolrServer( System.getenv("SOLR_INDEX_URL") );
			  SolrQuery query = new SolrQuery();
			  query.setQuery( "*:*");
			  Long start,end;
			  query.setRows(0);  // don't actually request any data
			  try {
				resp.getWriter().println(String.format(		"Searching against %s addresses",
						  									server.query(query).getResults().getNumFound()));
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resp.getWriter().println("Error searching on Solr");
			}			
		}else{
			String queryParam = req.getParameter("q");
			try{
			  CommonsHttpSolrServer server = new CommonsHttpSolrServer( System.getenv("SOLR_INDEX_URL") );
			  SolrQuery query = new SolrQuery();
			  query.setQuery( "text:"+queryParam );
			  query.set("wt", "json");
			  query.setFields("id","StreetAddress");
			  query.setFacet(true);
			  query.addFacetField("State");
			  
			  Long start,end;
			  start = System.currentTimeMillis();
			  QueryResponse queryResponse = server.query(query);
			  end = System.currentTimeMillis();
			  if("true".equalsIgnoreCase(req.getParameter("facet"))){
				  //resp.getWriter().println(String.format("Search took %s ms",(end-start)));
				  for (FacetField.Count entry:queryResponse.getFacetFields().get(0).getValues()) {
					  if(entry.getName().length()==2){
						  resp.getWriter().println(String.format("State: <b>%s</b> [<b>%s</b> matches found]",
								  								 entry.getName().toUpperCase(),
								  								 (entry.getCount()>0?entry.getCount():0)
								  								)
								  				);
					  }
				  }
				  
			  }else{
				  SolrDocument resultDoc;
				  Iterator<SolrDocument> iter = queryResponse.getResults().iterator();
				  while (iter.hasNext()) {
					  resultDoc= iter.next();
				      resp.getWriter().println((String) resultDoc.getFieldValue("StreetAddress"));
				  }
			  }
			  logger.debug("Completed writing to Stream");
			  logger.debug(String.format("Query time: %s ms",(end-start)));
			  logger.debug(String.format("Query Response: %s",queryResponse.getResponse().get("response")));
			 
			}catch(Exception ex){
				ex.printStackTrace();
				resp.getWriter().println("Error searching on Solr");
			}
		}
	}

}
