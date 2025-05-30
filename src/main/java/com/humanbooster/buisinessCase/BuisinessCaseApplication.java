package com.humanbooster.buisinessCase;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.humanbooster.buisinessCase.apirest.ApiApplication;

@SpringBootApplication
public class BuisinessCaseApplication {

	public static void main(String[] args) {
	// STARTS & CONNECTS TO DB
        SessionFactory sessionFactory = runDB();
        
        try {
            runServlet();
            SpringApplication.run(BuisinessCaseApplication.class, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static SessionFactory runDB(){
        System.out.println("Starting App...\n");
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();
        Metadata metadata = new MetadataSources(registry).buildMetadata();
        System.out.println("Connection Successfull !");
        return metadata.buildSessionFactory();
    }

    private static void runServlet() throws Exception {
		ResourceConfig config = new ApiApplication();
		
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");
        server.start();
        server.join();
    }

}
