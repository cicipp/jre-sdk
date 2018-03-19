package co.buybuddy.networking.ratelimiting;

import java.util.Date;

/**
 * A class that contains the quota information of the current user.
 */
public class Quota {

    /**
     The total consumed amount of quota by the user during the session.
     */
    private int consumedAmount;

    /**
     The maximum amount of consumable quota by the user during the session.
     */
    private int maximumAmount;

    /**
     The current quota reset date for the user.
     */
    private Date quotaResetDate;

    /**
     *Initializes a `Quota` class with the given consumed amount of quota, maximum amount of quota and the quota reset date.
     * @param consumedAmount The consumed amount of quota.
     * @param maximumAmount The maximum amount of quota available.
     * @param quotaResetDate The reset date of the quota.
     */
   public Quota(int consumedAmount, int maximumAmount, Date quotaResetDate) {

        this.consumedAmount = consumedAmount;
        this.maximumAmount = maximumAmount;
        this.quotaResetDate = quotaResetDate;
    }

    public int getConsumedAmount() {
        return consumedAmount;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public int getRemainingAmount() {
        return maximumAmount - consumedAmount;
    }

    public Date getQuotaResetDate() {
        return quotaResetDate;
    }

    /**
     A boolean value that indicates whether if any available quota is left or not.
     */
    public boolean isAvailable() {
        return consumedAmount < maximumAmount;
    }
}
