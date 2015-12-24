package tleaf.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class CachingConfig {//implements CachingConfigurer {

//    @Bean
//    public EhCacheManagerFactoryBean ehcache() {
//        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("classpath:cache.xml"));
//        return ehCacheManagerFactoryBean;
//    }

    //alternative to cache.xml
    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("myCache");
        cacheConfiguration.setMaxBytesLocalHeap("50M");
        cacheConfiguration.setTimeToLiveSeconds(1000L);
        //cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        //cacheConfiguration.setMaxEntriesLocalHeap(1000);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    public CacheManager cacheManager(net.sf.ehcache.CacheManager cm) {
        //ehCache CacheManager injected to Spring ECCManager (which implements Spring' CacheManager)
        return new EhCacheCacheManager(cm);
    }

//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        return new EhCacheCacheManager(ehCacheManager());
//    }
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return new SimpleKeyGenerator();
//    }
}

// todo @Cacheable methods / @CacheEvict - "изгнание" из кеша

// @Cacheable - Spring до запуска метода ищет в кеше возвращаемое методом значение.
//              Если значение найдено, то возвращается cached значение. Если нет, то вызывается метод и возвращаемое значение помещается в кэш.
// @CachePut - Spring должен поместить в кэш возвращаемое методом значение. Кэш не проверяется до вызова метода и метод всегда вызывается.
//             Полезно для preload кеша
// @CacheEvict - Spring должен “изгнать” 1 или N записей из кэша.
// @Caching - Аннотация группировки для применения нескольких кэш-аннотаций одновременно.
