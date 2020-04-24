package com.kingyee.prad.model;


import com.kingyee.prad.entity.PradScoreTransaction;

import java.util.List;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 用户积分明细列表
 */
public class UserScoreModel {
	private String month;
    private List<PradScoreTransaction> scoreTransactionList;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<PradScoreTransaction> getScoreTransactionList() {
        return scoreTransactionList;
    }

    public void setScoreTransactionList(List<PradScoreTransaction> scoreTransactionList) {
        this.scoreTransactionList = scoreTransactionList;
    }
}
