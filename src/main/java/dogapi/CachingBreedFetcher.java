package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = Objects.requireNonNull(fetcher, "fetcher must not be null");

    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException {

        if (breed == null) {
            throw new BreedFetcher.BreedNotFoundException("Breed must be non-empty");
        }
        String key = breed.trim().toLowerCase(Locale.ROOT);

        if (cache.containsKey(key)) {

            return new ArrayList<>(cache.get(key));
        }

        callsMade++;
        try {
            List<String> fetched = fetcher.getSubBreeds(breed);
            cache.put(key, new ArrayList<>(fetched));
            return new ArrayList<>(fetched);
        } catch (BreedFetcher.BreedNotFoundException e) {
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}