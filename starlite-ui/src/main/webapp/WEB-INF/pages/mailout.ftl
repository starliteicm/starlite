<#include "/starlite.ftl">
<html>
<head>
	<@enableJQuery/>
	<@enableJwysiwyg/>
	<script type="text/javascript" src="${request.contextPath}/js/tooltip.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.dimensions.js"></script>
	<script type="text/javascript">
	
	var selectedEmail = ${memsSelected.size()};
	var selectedDoc   = ${docsSelected.size()};
	
	function updateSelected(obj){
	  //alert("here "+obj.className);
	  if (obj.className == "email"){
	    if (obj.checked == true){
	      selectedEmail++;
	    }
	    else {
	    if(selectedEmail > 0){
	      selectedEmail--;
	      }
	    }
	    var esn = document.getElementById("emailSelectNo");
	    //alert(esn+" "+ selectedEmail);
	    esn.innerHTML = ""+selectedEmail;
	  }
	  else{
	  	if (obj.checked == true){
	      selectedDoc++;
	    }
	    else {
	     if(selectedDoc > 0){
	      selectedDoc--;
	      }
	    }
	    var dsn = document.getElementById("docSelectNo");
	    //alert(dsn+" "+selectedDoc);
	    dsn.innerHTML = ""+selectedDoc;
	  }
	};
	
	
        $(document).ready(function() {
        	$('.tooltip').tooltip({ 
    			track: true, 
    			delay: 10, 
    			showURL: false, 
    			showBody: " - ", 
    			extraClass: "pretty", 
    			fixPNG: true, 
    			opacity: 0.95,
    			top: 10,
    			left: 30
			});
			$('#wysiwyg').wysiwyg();
        });
	</script>
	<style type="text/css">
    #tooltip.pretty {
    position:absolute;
    margin-top:0px;
	font-family: Arial;
	border: none;
	width: 210px;
	padding:20px;
	height: 135px;
	opacity: 0.9;
	background: url('images/shadow.png');
}
#tooltip.pretty h3 {
	margin-bottom: 0.75em;
	font-size: 9pt;
	width: 210px;
	text-align: center;
}
#tooltip.pretty div { width: 210px; text-align: left; }
.heading {
  width:476px;
  height:20px;
  padding-bottom:5px;
  padding-top:10px;
}
    </style>
</head>
<body>


		<#macro treeSegment folder path rootPath>
			<#if folder.subFolders?size &gt; 0 || folder.docs?size &gt; 0>
			<ul>
				<#list folder.subFolders as subFolder>
					<li>${subFolder.path}<@treeSegment subFolder path+folder.path+'/' rootPath/></li>
				</#list>
				<#list folder.docs as doc>
				<#if docsSelected.contains(rootPath+path+folder.path+"/"+doc.name)>
					<li><input type="checkbox" CHECKED onclick="updateSelected(this)" name="docs" value="${rootPath}${path}${folder.path}/${doc.name}" /> <a href="${rootPath}${path}${folder.path}/${doc.name}" target="_blank">${doc.name}</a></li>
				<#else>	
					<li><input type="checkbox" onclick="updateSelected(this)" name="docs" value="${rootPath}${path}${folder.path}/${doc.name}" /> <a href="${rootPath}${path}${folder.path}/${doc.name}" target="_blank">${doc.name}</a></li>
				</#if>
				</#list>
			</ul>
			</#if>
		</#macro>
		
		
		<div style="padding-left:100px;padding-bottom:100px;">
		
		<form name="emailForm" action="mailout!sendMail.action" method="POST">
		
		<div class="heading"><span style="float:left;">From: (Email Reply Address)  </span><img class="tooltip" title=" - Email Reply Address: - please enter the Email address which will be displayed as from for emails and which can be replied to." style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/></div>
		<div ><input type="text" value="${from?if_exists}" name="from" style="width:476px;margin-bottom:10px;border:1px solid silver;" /></div>
		
		
		<div class="heading"><span style="float:left;">Subject: (Email Subject)  </span><img class="tooltip" title=" - Email Subject: - please enter the subject to be used on the email sent out." style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/></div>
		<div ><input type="text" value="${subject?if_exists}" name="subject" style="width:476px;margin-bottom:10px;border:1px solid silver;" /></div>
		
		<div class="heading"><span style="float:left;">To: (System) - [<span id="emailSelectNo">${memsSelected.size()}</span> selected] </span><img class="tooltip" title=" - System To: - Please check the boxes to the right of the member / members names you wish to send this mail to."  style="cursor:help;position:static;float:right;" src="images/icons/info.png"/>
		</div>
		
		<div style="height:100px; width:460px; overflow:auto; padding:5px; margin-bottom:10px; border: 1px solid silver;">
		<ul class="mktree" style="">
		<#list crew as member>
		<#if member.personal.email?exists>
		<#if memsSelected.contains(member.personal.email)>
		<li title="${member.personal.email?if_exists}"><input type="checkbox" CHECKED class="email" onclick="updateSelected(this)" name="mems"  value="${member.personal.email?if_exists}" /> ${member.code} - (${member.personal.firstName?if_exists} ${member.personal.lastName?if_exists})</li>
		<#else>
		<li title="${member.personal.email?if_exists}"><input type="checkbox" class="email" onclick="updateSelected(this)" name="mems"  value="${member.personal.email?if_exists}" /> ${member.code} - (${member.personal.firstName?if_exists} ${member.personal.lastName?if_exists})</li>
		</#if>
		</#if>
		</#list>
		</ul>
		</div>
		  
		
		<div class="heading"><span style="float:left;">Additional To: (Email Addresses)  </span><img class="tooltip" title="Additional To: Please enter the email address of people who you would like to include in this mailout but who are not on the system. Please comma / semicolon seperate any emails (e.g. to1@domain.com;to2@domain.com  to1@domain.com,to2@domain.com  )" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/></div>
		<div ><input type="text" name="emails" value="${emails?if_exists}" style="width:476px;margin-bottom:10px;border:1px solid silver;" /></div>
			
			<div class="heading"><span style="float:left;">Attach Documents: - [<span id="docSelectNo">${docsSelected.size()}</span> selected]</span><img class="tooltip" title=" - Attach Documents: - Please check the boxes to the right to select documents uploaded to the Distribution documents folder to be attached and sent with this mail" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/></div>
			
			<div style="height:100px; width:460px; overflow:auto; padding:5px; margin-bottom:10px; border: 1px solid silver;">
			<ul class="mktree" style="">
				<@treeSegment root '/' '${request.contextPath}/documents'/></li>
			</ul>
			</div>
			

<div class="heading"><span style="float:left">Message: 		</span><img class="tooltip" title="Email Message: - Please write you message here. For Images please only use a image URL (e.g. http://example.com/image.jpg), images from your own machine (browse) will not appear on the email. " style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/></div>
<textarea name="message" rows="11" cols="69" style="width:476px" id="wysiwyg">${message?if_exists}</textarea>

					  <div class="heading">					  
					  <button type="button" onclick="if(confirm('Are you sure you wish to send this email?')){this.disabled=true;$('#wysiwyg').wysiwyg('save');document.forms.emailForm.submit();}" class="smooth" style="position:relative;top:20px;float:right;"><img src="images/icons/email.png"/>Send Mail</button>
					  </div>
					  
					  </form>


</div>

</body>