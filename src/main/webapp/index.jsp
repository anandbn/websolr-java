<html>
<head>
<script type="text/javascript" src="resources/javascript/jquery-1.3.2.min.js"></script>
<script type='text/javascript' src='resources/javascript/jquery.bgiframe.min.js'></script>
<script type='text/javascript' src='resources/javascript/jquery.ajaxQueue.js'></script>
<script type='text/javascript' src='resources/javascript/thickbox-compressed.js'></script>
<script type='text/javascript' src='resources/javascript/jquery.autocomplete.js'></script>
<link rel="stylesheet" type="text/css" href="resources/css/main.css" />
<link rel="stylesheet" type="text/css" href="resources/css/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href="resources/css/thickbox.css" />
<script type="text/javascript">
$().ready(
	function() {
		function formatResult(row) {
			return row[0].replace(/(<.+?>)/gi, '');
		}
		$('#faceted_search').click(
			function(){
				$("#email").setOptions({extraParams:{facet:'true'}});
			}
		);
		$('#text_search').click(
			function(){
				$("#email").setOptions({extraParams:{facet:'false'}});
			}
		);
		
		$("#email").autocomplete("/search.solr", {selectFirst: false,scroll:false});
		$('#search_info').load('/search.solr?stats=true');
	}
);
</script>

	
</head>

<body>

<div id="content">
	
	<form autocomplete="off" id="searchform">
		<p>
		<div id="search_info" class="search_info">Searching # addresses</div>
		</p>
		<p align="center">
		<div class="search_info">
			<input name="search_type" value="text" type="radio" id="text_search" checked/>&nbsp;&nbsp;Text Search&nbsp;&nbsp;
            <input name="search_type" value="faceted" id="faceted_search" type="radio"/>&nbsp;&nbsp;Faceted Search
		</div>
		</p>
		<p>
		  <input type="text" id="email" size="500" maxlength="300" length="200"/>
	  </p>
  </form>
</div>
</body>

</body>
</html>