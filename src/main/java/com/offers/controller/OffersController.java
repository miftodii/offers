package com.offers.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.offers.dao.OffersDao;
import com.offers.model.Offer;
import com.offers.model.OfferSearch;
import com.offers.model.Status;

@RestController
@RequestMapping("/offers")
public class OffersController
{
    private static final Logger LOG = LoggerFactory.getLogger(OffersController.class);

    private final OffersDao offersDao;

    public OffersController(OffersDao offersDao)
    {
        this.offersDao = offersDao;
    }

    @GetMapping("/find")
    public ResponseEntity<List<Offer>> findOffers(
            @RequestParam(required = false) final String description,
            @RequestParam(required = false) final Double startPrice,
            @RequestParam(required = false) final Double endPrice,
            @RequestParam(required = false) final String currency,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final LocalDate endDate) {
        LOG.info("Find offers");

        final OfferSearch offerSearch =
                new OfferSearch(description, startPrice, endPrice, currency, startDate, endDate);
        final List<Offer> offers = offersDao.find(offerSearch);

        if (offers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        final List<Offer> result = new ArrayList<>();
        offers.forEach(offer -> {
            if (LocalDate.now().isAfter(offer.getExpirationDate())) {
                offersDao.markOfferAsExpired(offer.getId());
            } else {
                result.add(offer);
            }
        });

        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addOffer(@Valid @RequestBody final Offer offer) {
        LOG.info("Add new offer");

        if (LocalDate.now().isAfter(offer.getExpirationDate())) {
            offer.setStatus(Status.EXPIRED);
        }
        offersDao.save(offer);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/{offerId}/cancel")
    public ResponseEntity cancelOffer(@PathVariable(name = "offerId") final Long offerId) {
        LOG.info("Cancel offer: " + offerId);
        offersDao.cancel(offerId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
