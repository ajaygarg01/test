package com.unlimint.java.test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.opencsv.CSVReader;

@SpringBootApplication
public class JavaDeveloperTestApplication implements ApplicationRunner {

	AtomicInteger atomicId = new AtomicInteger();

	public static void main(String[] args) {
		SpringApplication.run(JavaDeveloperTestApplication.class, args);
	}

	private Future<List<Order>> parseCSV(ExecutorService service, String filename) {
		Callable<List<Order>> callableTask = () -> {
			try (CSVReader reader = new CSVReader(new FileReader(filename))) {
				List<Order> orders = new ArrayList<>();
				List<String[]> rows = reader.readAll();
				int rowCount = 0;
				for (String[] row : rows) {
					Integer id = atomicId.incrementAndGet();
					rowCount++;
					if (row.length != 4) {
						System.err.println(
								String.format("Some of the field is missing in row %d [file: %s]", rowCount, filename));
						continue;
					}
					if (!row[0].matches("\\d+")) {
						System.err.println(
								String.format("Order id should be number for row %d [file: %s]", rowCount, filename));
						continue;
					}
					if (!row[1].matches("\\d+")) {
						System.err.println(
								String.format("Amount should be number for row %d [file: %s]", rowCount, filename));
						continue;
					}
					Order order = new Order(Integer.parseInt(row[0]), Double.parseDouble(row[1]), row[2], row[3],
							filename, "OK", rowCount);
					order.setId(id);
					orders.add(order);
				}
				return orders;
			}
		};
		return service.submit(callableTask);
	}

	private Future<List<Order>> parseJson(ExecutorService service, String filename) {
		Callable<List<Order>> callableTask = () -> {
			JSONParser parser = new JSONParser();
			try {
				List<Order> orders = new ArrayList<>();
				JSONArray array = (JSONArray) parser.parse(new FileReader(filename));
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = array.iterator();
				int rowCount = 0;
				while (iterator.hasNext()) {
					Integer id = atomicId.incrementAndGet();
					rowCount++;
					JSONObject object = iterator.next();
					String orderId = object.get("orderId").toString(), amount = object.get("amount").toString();
					if (!orderId.matches("\\d+")) {
						System.err.println(
								String.format("Order id should be number for row %d [file: %s]", rowCount, filename));
						continue;
					}
					if (!amount.matches("\\d+")) {
						System.err.println(
								String.format("Amount should be number for row %d [file: %s]", rowCount, filename));
						continue;
					}
					Order order = new Order(Integer.parseInt(orderId), Double.parseDouble(amount),
							object.get("currency").toString(), object.get("comment").toString(), filename, "OK",
							rowCount);
					order.setId(id);
					orders.add(order);
				}
				return orders;
			} catch (IOException | ParseException e) {
				System.err.println(String.format("File(%s) not found or not able to parse", filename));
				return new ArrayList<>();
			}
		};
		return service.submit(callableTask);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(args.getNonOptionArgs().size());
		List<Order> orders = new ArrayList<>();
		for (String filename : args.getNonOptionArgs()) {
			if (filename.endsWith(".csv")) {
				orders.addAll(parseCSV(service, filename).get());
			} else if (filename.endsWith(".json")) {
				orders.addAll(parseJson(service, filename).get());
			} else {
				System.err.println(String.format("File(%s) format not supported", filename));
			}
		}

		orders.forEach(System.out::println);
	}
}
