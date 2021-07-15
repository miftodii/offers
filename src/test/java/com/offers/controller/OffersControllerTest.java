package com.offers.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.offers.dao.OffersDao;
import com.offers.model.Offer;
import com.offers.model.OfferSearch;
import com.offers.model.Status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OffersController.class)
public class OffersControllerTest
{
    @MockBean
    private OffersDao offersDao;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void initializeObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testFindOffers() throws Exception
    {
        final OfferSearch offerSearch = getOfferSearch();
        final List<Offer> offers = Arrays.asList(getOffer());

        Mockito.when(offersDao.find(offerSearch)).thenReturn(offers);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/find")
                .param("description", offerSearch.getDescription())
                .param("currency", offerSearch.getCurrency())
                .param("startDate", String.valueOf(offerSearch.getStartDate()))
                .param("endDate", String.valueOf(offerSearch.getEndDate()))
                .param("startPrice", String.valueOf(offerSearch.getStartPrice()))
                .param("endPrice", String.valueOf(offerSearch.getEndPrice())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("offer description")));
    }

    @Test
    public void testFindOffers_markAsExpired() throws Exception
    {
        final OfferSearch offerSearch = getOfferSearch();
        final Offer offer = getOffer();
        offer.setExpirationDate(LocalDate.now().minusMonths(5));
        final List<Offer> offers = Arrays.asList(offer);


        Mockito.when(offersDao.find(offerSearch)).thenReturn(offers);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/find")
                .param("description", offerSearch.getDescription())
                .param("currency", offerSearch.getCurrency())
                .param("startDate", String.valueOf(offerSearch.getStartDate()))
                .param("endDate", String.valueOf(offerSearch.getEndDate()))
                .param("startPrice", String.valueOf(offerSearch.getStartPrice()))
                .param("endPrice", String.valueOf(offerSearch.getEndPrice())))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testFindOffers_NoResults() throws Exception
    {
        final OfferSearch offerSearch = getOfferSearch();
        final List<Offer> offers = Arrays.asList();

        Mockito.when(offersDao.find(offerSearch)).thenReturn(offers);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/find")
                .param("description", offerSearch.getDescription())
                .param("currency", offerSearch.getCurrency())
                .param("startDate", String.valueOf(offerSearch.getStartDate()))
                .param("endDate", String.valueOf(offerSearch.getEndDate()))
                .param("startPrice", String.valueOf(offerSearch.getStartPrice()))
                .param("endPrice", String.valueOf(offerSearch.getEndPrice())))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testAddOffer() throws Exception
    {
        final Offer offer = getOffer();

        Mockito.doNothing().when(offersDao).save(offer);

        mockMvc.perform(MockMvcRequestBuilders.post("/offers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(offersDao).save(offer);
    }

    @Test
    public void testAddOffer_markAsExpired() throws Exception
    {
        final Offer offer = getOffer();
        offer.setExpirationDate(LocalDate.now().minusMonths(5));

        Mockito.doNothing().when(offersDao).save(offer);

        mockMvc.perform(MockMvcRequestBuilders.post("/offers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        offer.setStatus(Status.EXPIRED);

        Mockito.verify(offersDao).save(offer);
    }

    @Test
    public void testCancelOffer() throws Exception
    {
        Mockito.doNothing().when(offersDao).cancel(new Long(1));

        mockMvc.perform(MockMvcRequestBuilders.post("/offers/1/cancel"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(offersDao).cancel(new Long(1));
    }

    private Offer getOffer()
    {
        final Offer offer = new Offer();
        offer.setStatus(Status.ACTIVE);
        offer.setCancelled(false);
        offer.setCurrency("GBP");
        offer.setDescription("offer description");
        offer.setExpirationDate(LocalDate.now().plusMonths(3));
        offer.setPrice(300.00);

        return offer;
    }

    private OfferSearch getOfferSearch()
    {
        final OfferSearch offerSearch =
                new OfferSearch(
                        "offer description",
                        50.00,
                        500.00,
                        "GBP",
                        LocalDate.now(),
                        LocalDate.now().plusMonths(5));
        return offerSearch;
    }
}
