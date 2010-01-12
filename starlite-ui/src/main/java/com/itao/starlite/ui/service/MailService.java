package com.itao.starlite.ui.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.docs.dao.FolderDao;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.HtmlEmail;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;



/**
 * Class for sending e-mail messages based on Freemarker templates
 * or with attachments.
 * 
 * @author Ron Chan
 * @author Jonathan Elliott
 */
@Singleton
public class MailService {
    protected static final Log log = LogFactory.getLog(MailService.class);
    private Configuration freemarkerConfiguration;	
    
	@Inject
	private FolderDao folderDao;
    
    @Inject
    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
        log.info("inject freemarker");
        this.freemarkerConfiguration = freemarkerConfiguration;
        try {
        	log.info("multi-class loader");
        	freemarkerConfiguration.setDirectoryForTemplateLoading(new File("/home/admin/svnwork/starlite/starlite-core/src/main/java/com/itao/starlite/ftl"));
        	FileTemplateLoader ftl1 = new FileTemplateLoader(new File("/home/admin/svnwork/starlite/starlite-core/src/main/java/com/itao/starlite/ftl"));
        	FileTemplateLoader ftl2 = new FileTemplateLoader(new File("/home/admin/svnwork/starlite/starlite-ui/src/main/java/com/itao/starlite/ftl"));
        	ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "ftl");
        	TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ftl2, ctl };
        	MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);

			this.freemarkerConfiguration.setTemplateLoader(mtl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }

    /**
     * Convenience method for sending messages with attachments.
     * 
     * @param from
     * @param subject
     * @param emailAddresses
     * @param rr request reciept
     * @param attachments
     * @param message
     * @author Jonathan Elliott
     */
	public void sendAttachedMessage(String from, String subject, List<String> emailAddresses, boolean rr, Map<String, File> attachmentFiles,String message) {
		for(String emailAddress : emailAddresses){
			
	    	try{
	    		log.info("Sending attached Messsage to "+emailAddress+"");
	  
	  // Create the email message
	  HtmlEmail email = new HtmlEmail();
	  email.setHostName("mail.i-tao.com");
	  email.addTo(emailAddress);
   
	  email.setAuthentication("starlite@i-tao.com", "g04way");
      email.setFrom(from, "<Starlite> "+from);
	  
	  email.setSubject(subject);
	  
	  //request reciept
	  if(rr){
	    email.addHeader( "Return-Receipt-To","<Starlite> "+from);
	    email.addHeader( "Read-Receipt-To","<Starlite> "+from);
	    email.addHeader( "Disposition-Notification-To", "<Starlite> "+from);
	  }
	  
	  email.setBounceAddress(from);
	  
      email.setHtmlMsg("<HTML>"+message+"</HTML>");
      //email.setTextMsg(result);

	  // add the attachments
	  for(String attachmentFileName : attachmentFiles.keySet()){
    	  EmailAttachment attachment = new EmailAttachment();
    	  File attachmentFile = attachmentFiles.get(attachmentFileName);
    	  attachment.setPath(attachmentFile.getAbsolutePath());
    	  attachment.setDisposition(EmailAttachment.ATTACHMENT);
    	  attachment.setDescription(attachmentFileName);
    	  attachment.setName(attachmentFileName);
    	  email.attach(attachment);
    	  }

	  // send the email
	  //if(emailAddress.equals("jelliott@i-tao.com")){
	  email.send();
	  //}
	  
    } 
    catch (Exception e) {
        e.printStackTrace();
        log.error("error: ("+freemarkerConfiguration.getTemplateLoader()+") "+e);
    }
	}
	}
    
    /**
     * Convenience method for sending messages with attachments.
     * 
     * @param subject
     * @param emailAddresses
     * @param emailName
     * @param resource
     * @param template
     * @param model
     * @param attachmentName
     * @throws MessagingException
     * @author Jonathan Elliott
     */
    @SuppressWarnings("unchecked")
	public void sendAttachedMessage(String subject, String emailAddress, String emailName, File attachmentFile, String templateName, Map model) 
    {
    	
    	String result = null;
    	try{
    		log.info("Sending attached Messsage to "+emailAddress+" +BCC");
    	  EmailAttachment attachment = new EmailAttachment();
    	  attachment.setPath(attachmentFile.getAbsolutePath());
    	  attachment.setDisposition(EmailAttachment.ATTACHMENT);
    	  attachment.setDescription(attachmentFile.getName());
    	  attachment.setName(attachmentFile.getName());

    	  // Create the email message
    	  HtmlEmail email = new HtmlEmail();
    	  email.setHostName("mail.i-tao.com");
    	  email.addTo(emailAddress, emailName);
       
    	  email.setAuthentication("starlite@i-tao.com", "g04way");
          email.setFrom("dustyn@crewresource.biz", "Starlite");
    	  
    	  email.setSubject(subject);
    	  
    	  //request reciept
    	  email.addHeader( "Return-Receipt-To","<Starlite> dustyn@crewresource.biz");
    	  email.addHeader( "Read-Receipt-To","<Starlite> dustyn@crewresource.biz");
    	  email.addHeader( "Disposition-Notification-To", "<Starlite> dustyn@crewresource.biz");
    	  email.addBcc("jelliott@i-tao.com");
    	  email.setBounceAddress("dustyn@crewresource.biz");
    	  
          Template template = freemarkerConfiguration.getTemplate(templateName);
          template.setEncoding("UTF8");
          result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
          email.setHtmlMsg(result);
          //email.setTextMsg(result);

    	  // add the attachment
    	  email.attach(attachment);

    	  // send the email
    	  email.send();
    	  
        } 
        catch (Exception e) {
            e.printStackTrace();
            log.error("error: ("+freemarkerConfiguration.getTemplateLoader()+") "+e);
        }

    }
    
    
    /**
     * Convenience method for sending messages with attachments.
     * 
     * @param subject
     * @param emailAddresses
     * @param emailName
     * @param resource
     * @param template
     * @param model
     * @param attachmentName
     * @throws MessagingException
     * @author Jonathan Elliott
     */
    public void sendAttachedMessage(String from, String subject, Collection <String> emailAddresses, Map <String, File> attachmentFiles, String message) 
    {
    		
    		for(String emailAddress : emailAddresses){
    			
    	    	try{
    	    		log.info("Sending attached Messsage to "+emailAddress+"");
    	  
    	  // Create the email message
    	  HtmlEmail email = new HtmlEmail();
    	  email.setHostName("mail.i-tao.com");
    	  email.addTo(emailAddress);
       
    	  email.setAuthentication("starlite@i-tao.com", "g04way");
          email.setFrom(from, "Starlite - "+from);
    	  
    	  email.setSubject(subject);
    	  
    	  //request reciept
    	  email.addHeader( "Return-Receipt-To","<Starlite> dustyn@crewresource.biz");
    	  email.addHeader( "Read-Receipt-To","<Starlite> dustyn@crewresource.biz");
    	  email.addHeader( "Disposition-Notification-To", "<Starlite> dustyn@crewresource.biz");
    	  email.setBounceAddress("dustyn@crewresource.biz");
    	  
          email.setHtmlMsg("<HTML>"+message+"</HTML>");
          //email.setTextMsg(result);

    	  // add the attachments
    	  for(String attachmentFileName : attachmentFiles.keySet()){
        	  EmailAttachment attachment = new EmailAttachment();
        	  File attachmentFile = attachmentFiles.get(attachmentFileName);
        	  attachment.setPath(attachmentFile.getAbsolutePath());
        	  attachment.setDisposition(EmailAttachment.ATTACHMENT);
        	  attachment.setDescription(attachmentFileName);
        	  attachment.setName(attachmentFileName);
        	  email.attach(attachment);
        	  }

    	  // send the email
    	  //if(emailAddress.equals("jelliott@i-tao.com")){
    	  email.send();
    	  //}
    	  
        } 
        catch (Exception e) {
            e.printStackTrace();
            log.error("error: ("+freemarkerConfiguration.getTemplateLoader()+") "+e);
        }
    	}

    }
    
	public Document getDocumentByPath(String path, User user) throws InsufficientPrivilagesException {
		int lastSlash = path.lastIndexOf('/');
		String folderPath = path.substring(0, lastSlash);
		Folder f = folderDao.getFolderByPath("distribution", user);		
		//should allow reading as selection is always from the general folder.
		if (!f.canRead(user))
			throw new InsufficientPrivilagesException();
		
		Document doc = f.getDocumentByName(path.substring(lastSlash+1));
		return doc;
	}

    public Map<String,File> getFilesForAttachment(String[] filenames, User user){
    	String docsFolderPath = System.getProperty("user.home")+"/starlite-docs/";
    	Map<String,File> returnFiles = new TreeMap<String,File>();
    	if(filenames != null){
    	for(String file : filenames){
    		try{
    		Document doc = getDocumentByPath(file, user);
    		File f = new File(docsFolderPath+doc.getUuid());
    		returnFiles.put(doc.getName(),f);
    		}
    		catch(Exception e){
    		  //somehow the user was able to select files that they do not have access to...
    			log.error(e);
    		}
    	}
    	}
        return returnFiles;
    }
    
    public Map<String,Boolean> checkEmailAddrsOk(Collection<String> emails){
    	Map<String,Boolean> emailsNotOk = new TreeMap<String,Boolean>();
    	for(String email : emails){
    		//check email is ok.
    		
    		boolean ok = isValidEmailAddress(email);
    		if(!ok){
    			emailsNotOk.put(email,Boolean.FALSE);
    		}
    	}
    	return emailsNotOk;
    }
    
    public static boolean isValidEmailAddress(String aEmailAddress){
        if (aEmailAddress == null) return false;
        boolean result = true;
        try {
          @SuppressWarnings("unused")
		InternetAddress emailAddr = new InternetAddress(aEmailAddress);
          if ( ! hasNameAndDomain(aEmailAddress) ) {
            result = false;
          }
        }
        catch (AddressException ex){
          result = false;
        }
        return result;
      }

      private static boolean hasNameAndDomain(String aEmailAddress){
        String[] tokens = aEmailAddress.split("@");
        return 
         tokens.length == 2 &&
         (tokens[0].trim().length() > 2) && 
         (tokens[1].trim().length() > 2) ;
      }

    
    
    //=========================================================================
    
    /**
     * Send a simple message based on a Freemarker template.
     * @param msg
     * @param templateName
     * @param model
     */
    public void sendMessage(SimpleEmail email, String templateName, Map model) {
    	
        String result = null;

        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            template.setEncoding("UTF8");
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            email.setMsg(result);
            email.setHostName("mail.i-tao.com");
            email.setAuthentication("starlite@i-tao.com", "g04way");
            email.setFrom("dustyn@crewresource.biz", "Starlite");
            email.send();
        } 
        catch (Exception e) {
            e.printStackTrace();
		    System.out.println("error: ("+freemarkerConfiguration.getTemplateLoader()+") "+e);
        }
        
    }
    
    public void sendHTMLMessage(HtmlEmail email, String templateName, Map model) {
        String result = null;

        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            template.setEncoding("UTF8");
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            email.setHtmlMsg(result);            
            email.setHostName("mail.i-tao.com");
            email.setAuthentication("starlite@i-tao.com", "g04way");
            email.setFrom("dustyn@crewresource.biz", "Starlite");
            email.send();
        } 
        catch (Exception e) {
            e.printStackTrace();
		    System.out.println("error: ("+freemarkerConfiguration.getTemplateLoader()+") "+e);
        }
        
    }
    


    /**
     * Utility class for working with FreeMarker.
     * Provides convenience methods to process a FreeMarker template with a model.
     *
     * @author Juergen Hoeller
     * @since 14.03.2004
     */
    public static abstract class FreeMarkerTemplateUtils {

    	/**
    	 * Process the specified FreeMarker template with the given model and write
    	 * the result to the given Writer.
    	 * <p>When using this method to prepare a text for a mail to be sent with Spring's
    	 * mail support, consider wrapping IO/TemplateException in MailPreparationException.
    	 * @param model the model object, typically a Map that contains model names
    	 * as keys and model objects as values
    	 * @return the result as String
    	 * @throws IOException if the template wasn't found or couldn't be read
    	 * @throws freemarker.template.TemplateException if rendering failed
    	 * @see org.springframework.mail.MailPreparationException
    	 */
    	public static String processTemplateIntoString(Template template, Object model) throws IOException, TemplateException {
    		StringWriter result = new StringWriter();
    		template.process(model, result);
    		return result.toString();
    	}

    }




    
}