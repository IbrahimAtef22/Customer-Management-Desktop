package com.myprojects.customer.management.desktop.client;

import com.myprojects.customer.management.desktop.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class CustomerApiClient {
   
    private static final String BASE_URL = "http://localhost:8080/customers";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

   
    public List<Customer> getAllCustomers() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + " - " + response.message());
            }
            String json = response.body().string();
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Customer.class);
            return objectMapper.readValue(json, listType);
        }
    }

    
    public Customer createCustomer(Customer customer) throws IOException {
        String json = objectMapper.writeValueAsString(customer);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Create failed: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), Customer.class);
        }
    }

    
    public Customer updateCustomer(Long id, Customer customer) throws IOException {
        String json = objectMapper.writeValueAsString(customer);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .put(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Update failed: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), Customer.class);
        }
    }

    
    public void deleteCustomer(Long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Delete failed: " + response.code());
            }
        }
    }
    
}
