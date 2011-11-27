# Using WebSolr with Java on Heroku

This is a sample project that demonstrates how to use  [WebSolr add on](http://addons.heroku.com/websolr) with Java on Heroku. [WebSolr](http://websolr.com/) is essentially a hosted version of [Apache Solr](http://lucene.apache.org/solr/) which is a really fast and flexible search and indexing platform that can search/index any data. This project also uses the [JQuery Autocomplete](http://docs.jquery.com/Plugins/Autocomplete) plugin and [SolrJ](http://wiki.apache.org/solr/Solrj), a Java API to communicate with any Solr instance.

__*Note*__: This project is by no means a demonstration of all of Solr's capabilties. Please refer to the Solr wiki for more information on additional features and functionality that you can leverage with Solr. 

## Pre-requisites

- Basic knowledge of Java and Java servlets
- Knowledge of JQuery and using plugins
- Understanding of Solr and Solr Queries.

## How does it work?

Most of the magic happens in two main components:

1. `index.jsp` : This contains the JQuery autocomplete code and includes the Autocomplete Javascripts and CSS.
2. `SolrProxy.java` : This is a servlet that acts as a proxy the WebSolr index URL that is exposed on provision the add-on to your heroku account.

## Provisioning WebSolr

You can provision the WebSolr add-on to your Heroku app by using the command:
    
    $ heroku addons:add websolr:cobalt

__*Note*__: WebSolr is not a free add-on. If you provision their "cobalt" add-on you will be charged $20 per month. 

Once you provision this add-on, WebSolr will create a "default" index and add the WebSolr index URL to your Heroku config. To view the WebSolr index URL use the command:

    $ heroku config
    ...
    WEBSOLR_URL         => http://index.websolr.com/solr/...

Once you have the WebSolr index URL, you and send your Solr queries to this URL. To modify the Solr `schema.xml` and make updates to your Solr cofiguration, navigate to the WebSolr add-on under your app and that will open up a page to all the Solr indexes that you have defined. On click a specific index, you can look at the schema, synonym configurations, stop words etc.


## Using SolrJ

SolrJ is a pure java client to communicate with your Solr instance. Refer to [http://wiki.apache.org/solr/Solrj#Adding_Data_to_Solr](http://wiki.apache.org/solr/Solrj#Adding_Data_to_Solr) on how to add data to your index using Solr. Refer to [http://wiki.apache.org/solr/Solrj#Reading_Data_from_Solr](http://wiki.apache.org/solr/Solrj#Reading_Data_from_Solr) on how to use Solr queries using SolrJ.


## Using SolrJ and WebSolr

`SolrProxy.java` is the main Java servlet that does the communication with the WebSolr index URL. The code snippet that performs the query is below:


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

To see a working example of this, go to [http://websolr-java.herokuapp.com](http://websolr-java.herokuapp.com)

