package com.antoine.assignment.haud.builders;

import com.antoine.assignment.haud.entities.Customer;
import com.antoine.assignment.haud.exceptions.AssignmentException;
import lombok.extern.slf4j.Slf4j;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class CsvBuilder {

    public void exportToCSV(List<Customer> customers) throws IOException {

        List<CustomerCsv> customersToExport = new ArrayList<>();
        customers.forEach(customer -> {
            CustomerCsv customerCsv = new CustomerCsv(
                    customer.getId(),
                    customer.getUsername(),
                    customer.getName(),
                    customer.getSurname(),
                    customer.getAddress(),
                    customer.getBirthDate(),
                    customer.getSims().toString()
            );
            customersToExport.add(customerCsv);
        });

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        File file = new File("src/main/java/com/antoine/assignment/haud/exports/" + "users_" + currentDateTime + ".csv");

        boolean success = file.createNewFile();
        if (!success) {
            throw new AssignmentException("Error in exportToCSV", "Something went wrong when creating the new file");
        }

        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(new FileWriter(file),
                    CsvPreference.STANDARD_PREFERENCE);

            final String[] header = new String[]{"id", "username", "name", "surname",
                    "birthdate", "sims"};

            beanWriter.writeHeader(header);

            // write the beans
            ICsvBeanWriter finalBeanWriter = beanWriter;
            customersToExport.forEach(customer -> {
                try {
                    finalBeanWriter.write(customer, header);
                } catch (IOException e) {
                    throw new AssignmentException("Error while writing the csv", "Customer error : " + customer.toString());
                }
            });
        } finally {
            if (beanWriter != null) {
                beanWriter.close();
            }
        }
    }
}

