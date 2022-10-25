package com.provider.rest;

import com.provider.data.Provider;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@ApplicationPath("/resources")
@Path("provider")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProviderResource extends Application {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    private List<Provider> findProvider() {
        Query query =
                entityManager.createQuery("SELECT p FROM Provider");
        return query.getResultList();
    }

    @GET
    @Path("{id}")
    public Provider getProvider(@PathParam("id") Long id) {
        return entityManager.find(Provider.class, id);
    }

    @POST
    public Provider saveProvider(Provider provider) {
        if (provider.getId() == null) {
            Provider providerToSave = new Provider();
            providerToSave.setName(provider.getName());
            providerToSave.setEmail(provider.getEmail());
            providerToSave.setComment(provider.getComment());
            providerToSave.setCnpj(provider.getCnpj());
            entityManager.persist(provider);
        } else {
            Provider providerToUpdate = getProvider(provider.getId());
            providerToUpdate.setName(provider.getName());
            providerToUpdate.setEmail(provider.getEmail());
            providerToUpdate.setComment(provider.getComment());
            providerToUpdate.setCnpj(provider.getCnpj());
            provider = entityManager.merge(providerToUpdate);
        }

        return provider;
    }

    @DELETE
    @Path("{id}")
    public void deletePerson(@PathParam("id") Long id) {
        entityManager.remove(getProvider(id));
    }
}
