package com.guestworker.ui.activity.confirm.discount;

import com.guestworker.netwrok.Repository;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/5/7
 * @Describe
 */
public class DiscountPresenter {

    private Repository mRepository;

    @Inject
    public DiscountPresenter(Repository repository) {
        mRepository = repository;
    }



}
