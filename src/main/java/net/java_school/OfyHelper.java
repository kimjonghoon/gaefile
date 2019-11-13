package net.java_school;

import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import net.java_school.board.FileGroup;
import net.java_school.board.AttachFile;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run.  This is
 * required to let JSP's access Ofy.
 **/
public class OfyHelper implements ServletContextListener {
  public static void register() {
    ObjectifyService.register(FileGroup.class);
    ObjectifyService.register(AttachFile.class);
  }

  public void contextInitialized(ServletContextEvent event) {
    ServletContext sc = event.getServletContext();
    sc.log("OfyHelper: Init");
    // This will be invoked as part of a warmup request, or the first user
    // request if no warmup request was invoked.
    register();
  }

  public void contextDestroyed(ServletContextEvent event) {
    // App Engine does not currently invoke this method.
  }
}
