package edu.brown.cs.student.main.server.redlining.redliningData;

import static java.lang.Double.parseDouble;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
import java.util.concurrent.TimeUnit;

/**
 * A proxy class wrapping a RedliningDatasource in a cache. This should be used for querying
 * redlining data with configurable cache.
 */
public class CachedRedlining implements RedliningDatasource {
  private final RedliningDatasource wrappedSource;
  private final LoadingCache<String, GeoMapCollection> cacheFilteredQueries;

  /**
   * Proxy class: Wrap an instance of RedliningDatasource (of any kind) and cache its filtered
   * results.
   *
   * @param toWrap the RedliningDatasource to wrap
   * @param maxSize the maximum number of queries that should be kept in the cache at a time
   * @param minutesKept the maximum time in minutes that a query should be kept in the cache
   */
  public CachedRedlining(RedliningDatasource toWrap, int maxSize, int minutesKept) {
    this.wrappedSource = toWrap;
    this.cacheFilteredQueries =
        CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(minutesKept, TimeUnit.MINUTES)
            .build(
                // Strategy pattern: how should the cache behave when
                // it's asked for something it doesn't have?
                new CacheLoader<>() {
                  /**
                   * How to load data into the cache from the RedliningDatasource.
                   *
                   * @param bounds a String formatted "{minLat}, {maxLat}, {minLong}, {maxLong}"
                   * @return a GeoMapCollection describing the query results
                   */
                  @Override
                  public GeoMapCollection load(String bounds) {
                    String[] boxBounds = bounds.split(",");
                    // not error checking this because it is only called internally (always
                    // correctly)
                    double minLat = parseDouble(boxBounds[0]);
                    double maxLat = parseDouble(boxBounds[1]);
                    double minLong = parseDouble(boxBounds[2]);
                    double maxLong = parseDouble(boxBounds[3]);
                    // If this isn't yet present in the cache, load it:
                    return wrappedSource.getData(minLat, maxLat, minLong, maxLong);
                  }
                });
  }

  /**
   * Gathers all redlining data. Does not cache this result.
   *
   * @return the GeoMapCollection of complete redlining data
   */
  public GeoMapCollection getData() {
    return this.wrappedSource.getData();
  }

  /**
   * Gathers a filtered subset of the redlining data based on caller-specified bounding box. Caches
   * according to setting specified at this CachedRedlining object's creation.
   *
   * @param minLat the minimum longitude in the user's desired "bounding box"
   * @param maxLat the maximum longitude in the user's desired "bounding box"
   * @param minLong the minimum latitude in the user's desired "bounding box"
   * @param maxLong the maximum latitude in the user's desired "bounding box"
   * @return a GeoMapCollection composed of all the GeoMaps within the caller-defined "bounding box"
   */
  public GeoMapCollection getData(double minLat, double maxLat, double minLong, double maxLong) {
    String compositeArgs = minLat + "," + maxLat + "," + minLong + "," + maxLong;
    GeoMapCollection result = this.cacheFilteredQueries.getUnchecked(compositeArgs);
    return result;
  }
}
