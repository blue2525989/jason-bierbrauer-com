package com.jasonbierbrauer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;


@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	private static final String URL = "";
	@SuppressWarnings("unused")
	private static final String LOCAL = "";
	// add view controllers for pages that deal with spring security
    public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/login").setViewName("login");
    }
	
    @Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials("", "");
	}
 
	@SuppressWarnings("deprecation")
	@Bean
    public AmazonS3Client amazonS3Client(BasicAWSCredentials awsCredentials) {
        AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName("us-west-2")));
        return amazonS3Client;
    }
    
    
	// these are the credentials to login to database
	@Bean(name = "dataSource")
	 public DriverManagerDataSource dataSource() {
		 DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		 driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		 driverManagerDataSource.setUrl("jdbc:mysql://" + URL);
		 driverManagerDataSource.setUsername("");
		 driverManagerDataSource.setPassword("");
		 return driverManagerDataSource;
	 }
}
