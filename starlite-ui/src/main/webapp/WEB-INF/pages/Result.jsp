<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleIndigoSatDataCaptureProxyid" scope="session" class="com.proxime.webservices.IndigoSatDataCaptureProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleIndigoSatDataCaptureProxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleIndigoSatDataCaptureProxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleIndigoSatDataCaptureProxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        com.proxime.webservices.IndigoSatDataCapture getIndigoSatDataCapture10mtemp = sampleIndigoSatDataCaptureProxyid.getIndigoSatDataCapture();
if(getIndigoSatDataCapture10mtemp == null){
%>
<%=getIndigoSatDataCapture10mtemp %>
<%
}else{
        if(getIndigoSatDataCapture10mtemp!= null){
        String tempreturnp11 = getIndigoSatDataCapture10mtemp.toString();
        %>
        <%=tempreturnp11%>
        <%
        }}
break;
case 13:
        gotMethod = true;
        String dataInputString_1id=  request.getParameter("dataInputString16");
            java.lang.String dataInputString_1idTemp = null;
        if(!dataInputString_1id.equals("")){
         dataInputString_1idTemp  = dataInputString_1id;
        }
        java.lang.String putString13mtemp = sampleIndigoSatDataCaptureProxyid.putString(dataInputString_1idTemp);
if(putString13mtemp == null){
%>
<%=putString13mtemp %>
<%
}else{
        String tempResultreturnp14 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(putString13mtemp));
        %>
        <%= tempResultreturnp14 %>
        <%
}
break;
}
} catch (Exception e) { 
%>
exception: <%= e %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>