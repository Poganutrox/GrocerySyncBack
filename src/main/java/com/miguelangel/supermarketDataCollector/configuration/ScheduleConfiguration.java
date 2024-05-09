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

@Configuration
public class ScheduleConfiguration {
	@Autowired
	Set<IProvider> providers = new HashSet<>();

	@Autowired
	IProductService productService;

	private final Logger logger = LoggerFactory.getLogger(ScheduleConfiguration.class);

	//@Scheduled(cron = "0 */10 * ? * *") //CAMBIAR CRON A DIARIO!
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

	private void saveData(Set<Product> products) {
		boolean successProducts = productService.saveAllProducts(products);
		if (!successProducts) {
			logger.error("ERROR INSERTANDO PRODUCTOS");
		} else {
			logger.info("INSERTADO CON ÉXITO");
		}
	}
}
