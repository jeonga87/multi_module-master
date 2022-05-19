package kr.co.demo.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {

  /* 첨부파일 이미지 조회 */
  public final static String ATTACH_IMAGE_VIEW = "attachImageView";

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(Arrays.asList(
      /* 첨부파일 이미지 조회 */
      new ConcurrentMapCache(ATTACH_IMAGE_VIEW, CacheBuilder.newBuilder()
          .maximumSize(100)
          .expireAfterWrite(30, TimeUnit.MINUTES)
          .build().asMap(), false)
    ));
    return cacheManager;
  }

}