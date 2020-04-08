package com.example.demo.controller;

import com.example.demo.Phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
	public class MainController {
		private static final Logger log = LoggerFactory.getLogger(MainController.class);
	 
	    // ​​​​​​​
	    // Вводится (inject) из application.properties.
	    @Value("${welcome.message}")
	    private String message;

	    @Autowired
		JdbcTemplate jdbcTemplate;
	 
	    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	    public String index(Model model) {

			List<Phone> lp = new ArrayList<>();
			jdbcTemplate.query(
					"SELECT id, fio, num FROM phone",
					(rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("Fio"), rs.getString("num"))
					).forEach(phone -> lp.add(phone));

			model.addAttribute("message", message);
			model.addAttribute("phones", lp);
	 
	        return "index";
	    }

	    @RequestMapping(value = { "/create"}, method = RequestMethod.GET)
		public String createTable() {
	    	log.info("Creating tables");

	    	jdbcTemplate.execute("DROP TABLE phone IF EXISTS");
	    	jdbcTemplate.execute("CREATE TABLE phone(" +
					"id SERIAL, fio VARCHAR(255), num VARCHAR(255))");

	    	List<Object[]> splitUpNames = Arrays.asList("Флока 1111", "Иванов 222", "Петров 333", "Сидоров 4444").stream()
					.map(inStr -> inStr.split(" "))
					.collect(Collectors.toList());

	    	splitUpNames.forEach(inStr -> log.info(String.format("Добавлена запись %s %s", inStr[0], inStr[1])));

	    	jdbcTemplate.batchUpdate("INSERT INTO phone(fio, num) VALUES (?,?)", splitUpNames);

	    	jdbcTemplate.batchUpdate("INSERT INTO phone(fio, num) VALUES ('Флока1', '1111-1')");
	    	jdbcTemplate.batchUpdate("INSERT INTO phone(fio, num) VALUES ('Иванов1', '222-1')");

	    	log.info("Запрос записи, where fio = 'Флока':");

	    	jdbcTemplate.query(
	    			"SELECT id, fio, num FROM phone where fio = ?", new Object[] {"Флока"},
					(rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("Fio"), rs.getString("num"))
					).forEach(phone -> log.info(phone.toString()));

			log.info("Запрос записи, where fio like 'Флока%':");

			jdbcTemplate.query(
					"SELECT id, fio, num FROM phone where fio like ?", new Object[] {"Флока%"},
					(rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("Fio"), rs.getString("num"))
			).forEach(phone -> log.info(phone.toString()));

			return "create";

		}

}
