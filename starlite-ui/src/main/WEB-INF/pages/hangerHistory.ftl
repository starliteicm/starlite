<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<!--Author: Celeste Groenewald -->

<@enableHelp/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">

<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        
        function onSubmitWsColumn() {
            document.getElementById("saveButton").disabled = false;
        }
        
        function onInvokeExportAction(id) {
            
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/hangerHistory!createHistoryTable.action?' + parameterString;
        
          
           
        }
</script>
</script>

<style  type="text/css" >

div.container {width:98%; margin:1%;}
table#table1 {text-align:center; margin-left:auto; margin-right:auto; width:100px;}
tr,td {text-align:left;}


.buttonStyle {
display: inline;
background-color: transparent;
background-repeat: no-repeat;
margin: 0px auto;
padding: 0px 0px 0 0;
text-align: center;
font-family: Helvetica, Calibri, Arial, sans-serif;
font-size: 100%;
font-weight: bold;
text-decoration: none;
}

.buttonStyle:hover{
background-position: 0 -1px;
color: #FFFFFF;
}




table.pretty {
  margin: 1em 1em 1em 2em;
  background: whitesmoke;
  border-collapse: collapse;
  text-align:center;

}
table.pretty2 {
  margin: 0.1em 0.1em 0.1em 0.1em;
  background: whitesmoke;
  border-collapse: collapse;
  text-align:center;
  margin-left:auto; 
  margin-right:auto;

}
table.pretty th {
  border: 2px silver solid;
  padding: 0.1em;
  text-align:center;
  font-weight:bold;
}
table.pretty td {
  border: 2px silver solid;
  padding: 0.1em;
  background: white;
  text-align:center;

    width: 100px;
  height: 30px;
}


table.pretty2 td {
  border: 0px silver solid;
  padding: 0.1em;
  text-align:center;
      width: 100px;
  height: 30px;
}
table.pretty th {
  background: gainsboro;
  text-align:center;
 font-family:"Verdana", Times, serif;
 font-size:1.0em; 
text-align:center;
}
table.pretty caption {
  margin-left: inherit;
  margin-right: inherit;
}

</style>
<title>Hanger Management</title>
</head>
<body>

<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
        
   
    <div style="margin-left:150px;">
    <!--<form action="hangerHistory.action" enctype="multipart/form-data" method="post">
 
    <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick=" onclick="Test('Button One')" type="submit"><img src="images/icons/arrow_up.png"/>Testing</button>
   --> </form>
    </div>
  
    
    <hr class="clear"/>
    
    <br />


    
    <div style="margin-left:150px;">
    <@jmesa id="JobTicket"/>
    </div> 
  
</div>
<br/>
</#if>
 
</body>
</html>