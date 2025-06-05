package jon.modern_infra.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
class I18nConfig implements WebMvcConfigurer {
    @Value("${application.defaultLanguage:es_ES}")
    private String localeIdentifier;

    //Setting up the path of message_XX.properties files and default Encoding.
    //By default Spring initiates a messageSource bean searching on resources/messages_XX.properties
    //This can also be added via application.properties
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    //In order for our application to be able to determine which locale is currently in use, we need to add a LocaleResolver bean
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        if (localeIdentifier != null) {
            String[] parts = localeIdentifier.split("_");
            Locale locale = new Locale(parts[0], parts[1]);
            localeResolver.setDefaultLocale(locale);
        } else {
            //localeResolver.setDefaultLocale(Locale.getDefault()); //Gets the locale from the Java Virtual Machine.
            localeResolver.setDefaultLocale(new Locale("es", "ES"));
        }
        return localeResolver;
    }

    //An interceptor bean that will switch to a new locale based on the value of the lang parameter when present on the request
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setIgnoreInvalidLocale(true);
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    //In order for localeChangeInterceptor bean to take effect, we need to add it to the application's interceptor registry.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
