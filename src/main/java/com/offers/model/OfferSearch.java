package com.offers.model;

import java.time.LocalDate;
import java.util.Objects;

public class OfferSearch
{
    private String description;
    private Double startPrice;
    private Double endPrice;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;

    public OfferSearch(String description, Double startPrice, Double endPrice,
            String currency, LocalDate startDate, LocalDate endDate)
    {
        this.description = description;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getStartPrice()
    {
        return startPrice;
    }

    public void setStartPrice(Double startPrice)
    {
        this.startPrice = startPrice;
    }

    public Double getEndPrice()
    {
        return endPrice;
    }

    public void setEndPrice(Double endPrice)
    {
        this.endPrice = endPrice;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    @Override public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OfferSearch that = (OfferSearch) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(startPrice, that.startPrice) &&
                Objects.equals(endPrice, that.endPrice) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override public int hashCode()
    {
        return Objects.hash(
                description,
                startPrice,
                endPrice,
                currency,
                startDate,
                endDate);
    }

    @Override public String toString()
    {
        return "OfferSearch{" +
                "description='" + description + '\'' +
                ", startPrice=" + startPrice +
                ", endPrice=" + endPrice +
                ", currency='" + currency + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
