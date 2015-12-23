package tleaf.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager(net.sf.ehcache.CacheManager cm) {
        //ehCache CacheManager injected to Spring ECCManager (which implements Spring' CacheManager)
        return new EhCacheCacheManager(cm);
    }

    @Bean
    public EhCacheManagerFactoryBean ehCache() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("tleaf/cache/ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }

}

// todo @Cacheable methods / @CacheEvict - "изгнание" из кеша

// @Cacheable - Spring до запуска метода ищет в кеше возвращаемое методом значение.
//              Если значение найдено, то возвращается cached значение. Если нет, то вызывается метод и возвращаемое значение помещается в кэш.
// @CachePut - Spring должен поместить в кэш возвращаемое методом значение. Кэш не проверяется до вызова метода и метод всегда вызывается.
//             Полезно для preload кеша
// @CacheEvict - Spring должен “изгнать” 1 или N записей из кэша.
// @Caching - Аннотация группировки для применения нескольких кэш-аннотаций одновременно.
