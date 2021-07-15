package com.offers.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.offers.model.Offer;
import com.offers.model.OfferSearch;
import com.offers.model.Status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OffersDaoTest
{
    @Autowired
    private OffersDao offersDao;

    @Test
    public void testFindOffers() {
        final OfferSearch offerSearch = getOfferSearch();

        final List<Offer> offers = offersDao.find(offerSearch);

        Assertions.assertEquals(0, offers.size());
    }

    @Test
    public void testSaveOffer() {
        final Offer offer = getOffer();
        offersDao.save(offer);

        final List<Offer> offers = offersDao.find(getOfferSearch());

        Assertions.assertEquals(1, offers.size());
    }

    @Test
    public void testFindById() {
        final Optional<Offer> offer = offersDao.findById(new Long(3));

        Assertions.assertEquals("offer description", offer.get().getDescription());
        Assertions.assertEquals(199.99, offer.get().getPrice());
    }

    @Test
    public void testCancelOffer() {
        final Long id = new Long(3);
        offersDao.cancel(id);
        final Optional<Offer> offer = offersDao.findById(id);

        Assertions.assertEquals(true, offer.get().getCancelled());
    }

    @Test
    public void testMarkAsExpired() {
        final Long id = new Long(3);
        offersDao.markOfferAsExpired(id);
        final Optional<Offer> offer = offersDao.findById(id);

        Assertions.assertEquals(Status.EXPIRED, offer.get().getStatus());
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
                        LocalDate.now().plusMonths(3));
        return offerSearch;
    }
}
