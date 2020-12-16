package com.bharuwa.haritkranti.utils;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author anuragdhunna
 */
public class SimpleRestUtil {

    public static <T> T get(String url, Class<T> clazz) throws IOException, ExecutionException, InterruptedException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("Content-Type", MediaType.APPLICATION_JSON);
        return get(url,clazz, headers);
    }

    public static <T> T get(String url, Class<T> clazz, MultivaluedMap<String, Object> headers) {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        return webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).get(clazz);
    }

    public static <T,E> T get(String url, Class<T> clazz, Class<E> errorClazz,MultivaluedMap<String, Object> headers) {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).get();

        if (response.getStatus() == 200) {
            return response.readEntity(clazz);
        } else {
            E errorObj = response.readEntity(errorClazz);
            // throw error
            throw new RestErrorException(errorObj);
        }
    }

    public static <T> T post(String url, Object jsonBody, Class<T> clazz, MultivaluedMap<String, Object> headers) throws IOException, ExecutionException, InterruptedException {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        Entity entity = Entity.entity(jsonBody,MediaType.APPLICATION_JSON);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).post(entity);
        if (response.getStatus() == 200) {
            return response.readEntity(clazz);
        } else {
            // throw error
            throw new RestErrorException(response.getEntity());
        }
    }

    public static <T,E> T post(String url, Object jsonBody, Class<T> clazz, Class<E> errorClazz, MultivaluedMap<String, Object> headers) throws IOException, ExecutionException, InterruptedException {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        Entity entity = Entity.entity(jsonBody,MediaType.APPLICATION_JSON);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).post(entity);
        if (response.getStatus() == 200) {
            return response.readEntity(clazz);
        } else {
            E errorObj = response.readEntity(errorClazz);
            // throw error
            throw new RestErrorException(errorObj);
        }
    }

    public static <T,E> T put(String url, Object jsonBody, Class<T> clazz,Class<E> errorClazz, MultivaluedMap<String, Object> headers) throws IOException, ExecutionException, InterruptedException {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        Entity entity = Entity.entity(jsonBody,MediaType.APPLICATION_JSON);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).put(entity);
        if (response.getStatus() == 200) {
            return response.readEntity(clazz);
        } else {
            E errorObj = response.readEntity(errorClazz);
            // throw error
            throw new RestErrorException(errorObj);
        }
    }

    public static <T> T put(String url, Object jsonBody, Class<T> clazz, MultivaluedMap<String, Object> headers) throws IOException, ExecutionException, InterruptedException {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        Entity entity = Entity.entity(jsonBody,MediaType.APPLICATION_JSON);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).put(entity);
        if (response.getStatus() == 200) {
            return response.readEntity(clazz);
        } else {
            // throw error
            throw new RestErrorException(response.getEntity());
        }
    }

    public static <T> T delete(String url, Class<T> clazz, MultivaluedMap<String, Object> headers) {
        Client client = JerseyClientBuilder.newClient().register(JacksonJsonProvider.class);
        WebTarget webTarget = client.target(url);
        return webTarget.request().accept(MediaType.APPLICATION_JSON).headers(headers).delete(clazz);
    }

    public static InputStream getFile(String url) throws IOException, ExecutionException, InterruptedException {
        return null;
    }
}
