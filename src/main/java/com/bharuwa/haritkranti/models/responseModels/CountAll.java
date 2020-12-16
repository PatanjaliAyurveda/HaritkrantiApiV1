package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document
public class CountAll extends BaseObject {

    private long totalUsers;

    private long totalNationalManagers;

    private long totalActiveNationalManagers;

    private long totalStateManagers;

    private long totalActiveStateManagers;

    private long totalDistrictManagers;

    private long totalActiveDistrictManagers;

    private long totalAgentManagers;

    private long totalActiveAgentManagers;

    private long totalAgents;

    private long totalActiveAgents;

    private long totalFarmers;

    private long totalUserSignUps;

    private long totalKhasras;

    private long totalFemaleFarmers;

    private long totalMaleFarmers;

    private int totalDairyFarmers;

    private long totalBeekeepingFarmers;

    private int totalSericultureFarmers;

    private int totalHorticultureFarmers;

    private int totalCows;

    private int totalSheeps;

    private int totalGoats;

    private int totalBuffalos;


    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalAgents() {
        return totalAgents;
    }

    public void setTotalAgents(long totalAgents) {
        this.totalAgents = totalAgents;
    }

    public long getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(long totalFarmers) {
        this.totalFarmers = totalFarmers;
    }

    public long getTotalUserSignUps() {
        return totalUserSignUps;
    }

    public void setTotalUserSignUps(long totalUserSignUps) {
        this.totalUserSignUps = totalUserSignUps;
    }

    public long getTotalKhasras() {
        return totalKhasras;
    }

    public void setTotalKhasras(long totalKhasras) {
        this.totalKhasras = totalKhasras;
    }

    public long getTotalFemaleFarmers() {
        return totalFemaleFarmers;
    }

    public void setTotalFemaleFarmers(long totalFemaleFarmers) {
        this.totalFemaleFarmers = totalFemaleFarmers;
    }

    public long getTotalMaleFarmers() {
        return totalMaleFarmers;
    }

    public void setTotalMaleFarmers(long totalMaleFarmers) {
        this.totalMaleFarmers = totalMaleFarmers;
    }

    public int getTotalDairyFarmers() {
        return totalDairyFarmers;
    }

    public void setTotalDairyFarmers(int totalDairyFarmers) {
        this.totalDairyFarmers = totalDairyFarmers;
    }

    public long getTotalBeekeepingFarmers() {
        return totalBeekeepingFarmers;
    }

    public void setTotalBeekeepingFarmers(long totalBeekeepingFarmers) {
        this.totalBeekeepingFarmers = totalBeekeepingFarmers;
    }

    public int getTotalSericultureFarmers() {
        return totalSericultureFarmers;
    }

    public void setTotalSericultureFarmers(int totalSericultureFarmers) {
        this.totalSericultureFarmers = totalSericultureFarmers;
    }

    public int getTotalHorticultureFarmers() {
        return totalHorticultureFarmers;
    }

    public void setTotalHorticultureFarmers(int totalHorticultureFarmers) {
        this.totalHorticultureFarmers = totalHorticultureFarmers;
    }

    public int getTotalCows() {
        return totalCows;
    }

    public void setTotalCowss(int totalCows) {
        this.totalCows = totalCows;
    }

    public int getTotalSheeps() {
        return totalSheeps;
    }

    public void setTotalSheeps(int totalSheeps) {
        this.totalSheeps = totalSheeps;
    }

    public int getTotalGoats() {
        return totalGoats;
    }

    public void setTotalGoats(int totalGoats) {
        this.totalGoats = totalGoats;
    }

    public int getTotalBuffalos() {
        return totalBuffalos;
    }

    public void setTotalBuffalos(int totalBuffalos) {
        this.totalBuffalos = totalBuffalos;
    }

    public long getTotalStateManagers() {
        return totalStateManagers;
    }

    public void setTotalStateManagers(long totalStateManagers) {
        this.totalStateManagers = totalStateManagers;
    }

    public long getTotalDistrictManagers() {
        return totalDistrictManagers;
    }

    public void setTotalDistrictManagers(long totalDistrictManagers) {
        this.totalDistrictManagers = totalDistrictManagers;
    }

    public long getTotalAgentManagers() {
        return totalAgentManagers;
    }

    public void setTotalAgentManagers(long totalAgentManagers) {
        this.totalAgentManagers = totalAgentManagers;
    }

    public long getTotalActiveStateManagers() {
        return totalActiveStateManagers;
    }

    public void setTotalActiveStateManagers(long totalActiveStateManagers) {
        this.totalActiveStateManagers = totalActiveStateManagers;
    }

    public long getTotalActiveDistrictManagers() {
        return totalActiveDistrictManagers;
    }

    public void setTotalActiveDistrictManagers(long totalActiveDistrictManagers) {
        this.totalActiveDistrictManagers = totalActiveDistrictManagers;
    }

    public long getTotalActiveAgentManagers() {
        return totalActiveAgentManagers;
    }

    public void setTotalActiveAgentManagers(long totalActiveAgentManagers) {
        this.totalActiveAgentManagers = totalActiveAgentManagers;
    }

    public long getTotalActiveAgents() {
        return totalActiveAgents;
    }

    public void setTotalActiveAgents(long totalActiveAgents) {
        this.totalActiveAgents = totalActiveAgents;
    }

    public void setTotalCows(int totalCows) {
        this.totalCows = totalCows;
    }

    public long getTotalNationalManagers() {
        return totalNationalManagers;
    }

    public void setTotalNationalManagers(long totalNationalManagers) {
        this.totalNationalManagers = totalNationalManagers;
    }

    public long getTotalActiveNationalManagers() {
        return totalActiveNationalManagers;
    }

    public void setTotalActiveNationalManagers(long totalActiveNationalManagers) {
        this.totalActiveNationalManagers = totalActiveNationalManagers;
    }
}
