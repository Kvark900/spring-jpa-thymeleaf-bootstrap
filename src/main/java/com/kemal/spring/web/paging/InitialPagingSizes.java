package com.kemal.spring.web.paging;

public class InitialPagingSizes {
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = { 5, 10, 20 };

    public static int getButtonsToShow() {
        return BUTTONS_TO_SHOW;
    }

    public static int getInitialPage() {
        return INITIAL_PAGE;
    }

    public static int getInitialPageSize() {
        return INITIAL_PAGE_SIZE;
    }

    public static int[] getPageSizes() {
        return PAGE_SIZES;
    }
}
