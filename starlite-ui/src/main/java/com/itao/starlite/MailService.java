package com.itao.starlite;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;



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
    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
        log.info("inject freemarker");
        this.freemarkerConfiguration = freemarkerConfiguration;
        try {
        	log.info("multi-class loader");
        	freemarkerConfiguration.setDirectoryForTemplateLoading(new File("/home/admin/svnwork/starlite/starlite-core/src/main/java/com/itao/starlite/ftl"));
        	FileTemplateLoader ftl1 = new FileTemplateLoader(new File("/home/admin/svnwork/starlite/starlite-core/src/main/java/com/itao/starlite/ftl"));
        	FileTemplateLoader ftl2 = new FileTemplateLoader(new File("/home/admin/svnwork/starlite/starlite-ui/src/main/java/com/itao/starlite/ftl"));
        	FileTemplateLoader ftl3 = new FileTemplateLoader(new File("/home/admin/svnwork/starlite/starlite-ui/target/starlite-ui/classes/com/itao/starlite/ftl"));
        	ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "ftl");
        	TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ftl2, ftl3, ctl };
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
    public void sendAttachedMessage(String subject, String emailAddress, String emailName, File attachmentFile, String templateName, Map<String, Object> model) 
    {
    	
    	String result = null;
    	try{
    		log.info("Sending attached Messsage");
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
          email.setFrom("support@i-tao.com", "Starlite");
    	  
    	  email.setSubject(subject);
    	  
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
     * Send a simple message based on a Freemarker template.
     * @param msg
     * @param templateName
     * @param model
     */
    public void sendMessage(SimpleEmail email, String templateName, Map<String, Object> model) {
    	
        String result = null;

        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            template.setEncoding("UTF8");
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            email.setMsg(result);
            email.setHostName("mail.i-tao.com");
            email.setAuthentication("starlite@i-tao.com", "g04way");
            email.setFrom("support@i-tao.com", "Starlite");
            email.send();
        } 
        catch (Exception e) {
            e.printStackTrace();
		    System.out.println("error: ("+freemarkerConfiguration.getTemplateLoader()+") "+e);
        }
        
    }
    
    public void sendHTMLMessage(HtmlEmail email, String templateName, Map<String, Object> model) {
        String result = null;

        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            template.setEncoding("UTF8");
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            email.setHtmlMsg(result);            
            email.setHostName("mail.i-tao.com");
            email.setAuthentication("starlite@i-tao.com", "g04way");
            email.setFrom("support@i-tao.com", "Starlite");
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