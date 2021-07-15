package com.offers.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "offer")
public class Offer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Description is required")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Price is required")
    @Column(name = "price")
    private Double price;

    @NotBlank(message = "Currency is required")
    @Column(name = "currency")
    private String currency;

    @NotBlank(message = "Expiration date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @NotBlank(message = "Status is required")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "cancelled")
    private Boolean cancelled;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public LocalDate getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Boolean getCancelled()
    {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    @Override public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) &&
                Objects.equals(description, offer.description) &&
                Objects.equals(price, offer.price) &&
                Objects.equals(currency, offer.currency) &&
                Objects.equals(expirationDate, offer.expirationDate) &&
                status == offer.status &&
                Objects.equals(cancelled, offer.cancelled);
    }

    @Override public int hashCode()
    {
        return Objects.hash(
                id,
                description,
                price,
                currency,
                expirationDate,
                status,
                cancelled);
    }

    @Override public String toString()
    {
        return "Offer{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", expirationDate=" + expirationDate +
                ", status=" + status +
                ", cancelled=" + cancelled +
                '}';
    }
}
