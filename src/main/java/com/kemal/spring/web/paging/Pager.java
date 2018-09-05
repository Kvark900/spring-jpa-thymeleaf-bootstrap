package com.kemal.spring.web.paging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Pager {

    private int buttonsToShow = 5;

    private int startPage;

    private int endPage;

    private List<Integer> pageSizesToShow = new ArrayList<>();

    public Pager(int totalPages, int currentPage, int buttonsToShow, long totalSize) {

        setButtonsToShow(buttonsToShow);
        setPageSizesToShow(totalSize);

        int halfPagesToShow = getButtonsToShow() / 2;

        if (totalPages <= getButtonsToShow()) {
            setStartPage(1);
            setEndPage(totalPages);

        } else if (currentPage - halfPagesToShow <= 0) {
            setStartPage(1);
            setEndPage(getButtonsToShow());

        } else if (currentPage + halfPagesToShow == totalPages) {
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(totalPages);

        } else if (currentPage + halfPagesToShow > totalPages) {
            setStartPage(totalPages - getButtonsToShow() + 1);
            setEndPage(totalPages);

        } else {
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(currentPage + halfPagesToShow);
        }

    }

    public int getButtonsToShow() {
        return buttonsToShow;
    }

    public void setButtonsToShow(int buttonsToShow) {
        if (buttonsToShow % 2 != 0) {
            this.buttonsToShow = buttonsToShow;
        } else {
            throw new IllegalArgumentException("Must be an odd value!");
        }
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    @Override
    public String toString() {
        return "Pager [startPage=" + startPage + ", endPage=" + endPage + "]";
    }

    public List<Integer> getPageSizesToShow() {
        return pageSizesToShow;
    }

    public String getPageSizesToShowInJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(pageSizesToShow);
    }

    public void setPageSizesToShow(List<Integer> pageSizesToShow) {
        this.pageSizesToShow = pageSizesToShow;
    }

    public void setPageSizesToShow(long totalSize) {
        this.pageSizesToShow = Arrays.stream(InitialPagingSizes.PAGE_SIZES).boxed()
                .filter(pageSize -> totalSize > pageSize)
                .collect(Collectors.toList());
    }
}
