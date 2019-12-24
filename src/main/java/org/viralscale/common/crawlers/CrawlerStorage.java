package org.viralscale.common.crawlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrawlerStorage<T> {
    private static Logger logger = LoggerFactory.getLogger(CrawlerStorage.class);

    private Set<T> activePosts;
    private Set<T> discardedPosts;

    public CrawlerStorage() {
        this.activePosts = new HashSet<>();
        this.discardedPosts = new HashSet<>();
    }

    public List<T> getActivePosts() {
        return new ArrayList<>(activePosts);
    }

    public List<T> getDiscardedPosts() {
        return new ArrayList<>(discardedPosts);
    }

    public void addToActivePostsIfNotInactive(List<T> newPosts) {
        newPosts
                .stream()
                .filter(np -> !discardedPosts.contains(np))
                .forEachOrdered(p -> activePosts.add(p));
    }

    public void addToActivePostsIfNotInactive(T newPost) {
        addToActivePostsIfNotInactive(List.of(newPost));
    }

    public void markAsInactive(T post) {
        if (this.activePosts.contains(post)) {
            activePosts.remove(post);

            discardedPosts.add(post);

            logger.warn("Marking post " + post + " as inactive");
        }
    }
}
