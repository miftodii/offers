package com.offers.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.offers.model.Offer;
import com.offers.model.OfferSearch;
import com.offers.model.Status;

@Repository
public class OffersDao
{
    private final EntityManager em;

    public OffersDao(EntityManager entityManager)
    {
        this.em = entityManager;
    }

    public List<Offer> find(final OfferSearch offerSearch) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Offer> cq = cb.createQuery(Offer.class);
        final Root<Offer> offer = cq.from(Offer.class);
        final List<Predicate> predicates = new ArrayList<>();

        if (null != offerSearch.getDescription()) {
            predicates.add(cb.equal(offer.get("description"), offerSearch.getDescription()));
        }

        if (null != offerSearch.getStartPrice() && null != offerSearch.getEndPrice()) {
            predicates.add(cb.between(offer.get("price"), offerSearch.getStartPrice(), offerSearch.getEndPrice()));
        }

        if (null != offerSearch.getCurrency()) {
            predicates.add(cb.equal(offer.get("currency"), offerSearch.getCurrency()));
        }

        if (null != offerSearch.getStartDate() && null != offerSearch.getEndDate()) {
            predicates.add(cb.between(offer.get("expirationDate"), offerSearch.getStartDate(), offerSearch.getEndDate()));
        }

        predicates.add(cb.equal(offer.get("cancelled"), false));
        predicates.add(cb.equal(offer.get("status"), Status.ACTIVE));

        cq.where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(cq).getResultList();
    }

    @Transactional
    public void save(final Offer offer) {
        em.persist(offer);
    }

    public Optional<Offer> findById(final Long id) {
        final Offer offer = (Offer) em.createQuery("select o from Offer o where o.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        return Optional.of(offer);
    }

    @Transactional
    public void cancel(final Long id) {
        final Optional<Offer> offer = findById(id);
        if (offer.isPresent()) {
            em.createQuery("update Offer set cancelled = true where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }

    @Transactional
    public void markOfferAsExpired(final Long id) {
        final Optional<Offer> offer = findById(id);
        if (offer.isPresent()) {
            em.createQuery("update Offer set status = :status where id = :id")
                    .setParameter("status", Status.EXPIRED)
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }
}
