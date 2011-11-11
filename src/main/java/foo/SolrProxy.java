package foo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrProxy extends HttpServlet {
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(SolrProxy.class);
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String queryParam = req.getParameter("q");
		try{
			  CommonsHttpSolrServer server = new CommonsHttpSolrServer( System.getenv("SOLR_INDEX_URL") );
			  SolrQuery query = new SolrQuery();
			  query.setQuery( "text:"+queryParam );
			  query.set("wt", "json");
			  query.setFields("id","StreetAddress");
			  Long start,end;
			  start = System.currentTimeMillis();
			  QueryResponse queryResponse = server.query(query);
			  end = System.currentTimeMillis();
			  Iterator<SolrDocument> iter = queryResponse.getResults().iterator();
			  logger.debug(String.format("Query time: %s ms",(end-start)));
			  logger.debug(String.format("Query Response: %s",queryResponse.getResponse().get("response")));
			  SolrDocument resultDoc;
			  while (iter.hasNext()) {
				  resultDoc= iter.next();
			      resp.getWriter().println((String) resultDoc.getFieldValue("StreetAddress"));
			    }
			    logger.debug("Completed writing to Stream");
		}catch(Exception ex){
			resp.getWriter().println("Error searching on Solr");
		}

	}

}
