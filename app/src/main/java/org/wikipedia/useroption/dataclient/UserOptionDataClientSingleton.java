package org.wikipedia.useroption.dataclient;

import org.wikipedia.Site;

public final class UserOptionDataClientSingleton {
    public static UserOptionDataClient instance() {
        return LazyHolder.INSTANCE;
    }

    private UserOptionDataClientSingleton() { }

    private static class LazyHolder {
        private static final UserOptionDataClient INSTANCE = instance();

        private static UserOptionDataClient instance() {
            Site site = new Site("meta.wikimedia.org", "");
            return new DefaultUserOptionDataClient(site);
        }
    }
}