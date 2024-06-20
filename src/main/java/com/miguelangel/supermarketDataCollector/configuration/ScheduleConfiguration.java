package com.miguelangel.supermarketDataCollector.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.model.IProvider;
import com.miguelangel.supermarketDataCollector.services.IProductService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration class for scheduling data retrieval from various providers.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
@Configuration
public class ScheduleConfiguration {
	@Autowired
	Set<IProvider> providers = new HashSet<>();

	@Autowired
	IProductService productService;

	private final Logger logger = LoggerFactory.getLogger(ScheduleConfiguration.class);

	/**
	 * Scheduled task to retrieve data from all registered providers and save it into the database.
	 * Runs daily at midday.
	 */
	@Scheduled(cron = "0 0 12 * * ?") //DAILY CRON AT MIDDAY
	private void scheduleDataRetrieving() throws InterruptedException {
		Set<Thread> supermarketProviders = new HashSet<>();
		Set<Product> products = Collections.synchronizedSet(new HashSet<>());

		for (IProvider p : providers) {
			Thread supermarket = new Thread(() -> {
				Set<Product> newProducts = p.collectProducts();

				synchronized (products) {
					products.addAll(newProducts);
			}

					logger.info("{}-> {} productos", p.getSupermarket().getName(), newProducts.size());
			});
			supermarketProviders.add(supermarket);
			supermarket.start();
		}

		for (Thread thread : supermarketProviders) {
			thread.join();
		}

		saveData(products);
	}

	/**
	 * Saves the retrieved products into the database.
	 *
	 * @param products The set of products to be saved.
	 */
	private void saveData(Set<Product> products) {
		boolean successProducts = productService.saveAllProducts(products);
		if (!successProducts) {
			logger.error("ERROR INSERTANDO PRODUCTOS");
		} else {
			logger.info("INSERTADO CON ÉXITO");
		}
	}
}
