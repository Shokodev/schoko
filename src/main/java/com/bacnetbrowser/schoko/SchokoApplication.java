package com.bacnetbrowser.schoko;


import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Start application and spring boot
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@SpringBootApplication
@EnableConfigurationProperties(SettingsHandler.class)
public class SchokoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchokoApplication.class, args);

	}
}
